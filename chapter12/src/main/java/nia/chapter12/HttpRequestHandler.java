package nia.chapter12;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.DefaultFileRegion;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.DefaultHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.stream.ChunkedNioFile;
import java.io.File;
import java.io.RandomAccessFile;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * @author mawenlong
 * @date 2018/09/24
 *
 * 扩展 SimpleChannelInboundHandler 以处理 FullHttpRequest 消息
 */
public class HttpRequestHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

  private final String wsUri;
  private static final File INDEX;

  static {
    URL location = HttpRequestHandler.class
        .getProtectionDomain()
        .getCodeSource()
        .getLocation();
    try {
      String path = location.toURI() + "index.html";
      path = !path.contains("file:") ? path : path.substring(5);
      INDEX = new File(path);
    } catch (URISyntaxException e) {
      throw new IllegalStateException("Unable to locate index.html", e);
    }

  }

  public HttpRequestHandler(String wsUri) {
    this.wsUri = wsUri;
  }

  @Override
  protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
//    1) 如果请求了 WebSocket 协议升级，则增加引用计数（调用 retain()方法），并将它传递给下一 个 ChannelInboundHandler
    if (wsUri.equalsIgnoreCase(request.uri())) {
      ctx.fireChannelRead(request.retain());
    } else {
//      2)处理100 Continue请求以符合HTTP1.1规范
      if (HttpUtil.is100ContinueExpected(request)) {
        send100Continue(ctx);
      }
//      读取index.html
      RandomAccessFile file = new RandomAccessFile(INDEX, "r");
      HttpResponse response = new DefaultHttpResponse(request.protocolVersion(),
          HttpResponseStatus.OK);
      response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html;charset=UTF-8");
      boolean keepAlive = HttpUtil.isKeepAlive(request);
//      如果请求添加了keep-alive 则添加所需要的Http头信息
      if (keepAlive) {
        response.headers().set(HttpHeaderNames.CONTENT_LENGTH, file.length());
        response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
      }
//      3)将HttpResponse写到客户端
      ctx.write(response);
//      4)将index.html写到客户端
      if (ctx.pipeline().get(SslHandler.class) == null) {
        ctx.write(new DefaultFileRegion(file.getChannel(), 0, file.length()));
      } else {
        ctx.write(new ChunkedNioFile(file.getChannel()));
      }
//      5)写LastHttpContent并从刷到客户端
      ChannelFuture future = ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);
//      6) 如果没有请求keep-alive，则在写操作完成后关闭 Channel
      if (!keepAlive) {
        future.addListener(ChannelFutureListener.CLOSE);
      }
    }
  }

  public static void send100Continue(ChannelHandlerContext ctx) {
    FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
        HttpResponseStatus.CONTINUE);
    ctx.writeAndFlush(response);
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    cause.printStackTrace();
    ctx.close();
  }
}
