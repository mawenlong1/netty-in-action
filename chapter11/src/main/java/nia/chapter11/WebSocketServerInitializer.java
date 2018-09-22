package nia.chapter11;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.ContinuationWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;

/**
 * @author mawenlong
 * @date 2018/09/22
 *
 * 在服务器端支持 WebSocket
 */
public class WebSocketServerInitializer extends ChannelInitializer<Channel> {

  @Override
  protected void initChannel(Channel ch) throws Exception {
    ch.pipeline()
        .addLast(new HttpServerCodec(),
            new HttpObjectAggregator(65536),
//           如果被请求的端点是"/websocket"，则处理该升级握手
            new WebSocketServerProtocolHandler("/websocket"),
//            TextFrameHandlerText处理text frame
            new TextFrameHandler(),
            new BinaryFrameHandler(),
            new ContinuationFrameHandler());
  }


  public static final class TextFrameHandler extends
      SimpleChannelInboundHandler<TextWebSocketFrame> {

    @Override
    public void channelRead0(ChannelHandlerContext ctx,
        TextWebSocketFrame msg) throws Exception {
//       处理 text frame
    }
  }

  public static final class BinaryFrameHandler extends
      SimpleChannelInboundHandler<BinaryWebSocketFrame> {

    @Override
    public void channelRead0(ChannelHandlerContext ctx,
        BinaryWebSocketFrame msg) throws Exception {
//       处理 binary frame
    }
  }

  public static final class ContinuationFrameHandler extends
      SimpleChannelInboundHandler<ContinuationWebSocketFrame> {

    @Override
    public void channelRead0(ChannelHandlerContext ctx,
        ContinuationWebSocketFrame msg) throws Exception {
//       处理 continuation frame
    }
  }
}
