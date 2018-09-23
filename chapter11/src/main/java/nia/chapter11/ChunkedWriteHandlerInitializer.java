package nia.chapter11;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.stream.ChunkedStream;
import io.netty.handler.stream.ChunkedWriteHandler;
import java.io.File;
import java.io.FileInputStream;

/**
 * @author mawenlong
 * @date 2018/09/23
 *
 * 使用 ChunkedStream 传输文件内容
 */
public class ChunkedWriteHandlerInitializer extends ChannelInitializer<Channel> {

  private final File file;
  private final SslContext sslContext;

  public ChunkedWriteHandlerInitializer(File file, SslContext sslContext) {
    this.file = file;
    this.sslContext = sslContext;
  }


  @Override
  protected void initChannel(Channel ch) throws Exception {
    ChannelPipeline pipeline = ch.pipeline();

    pipeline.addLast(new SslHandler(sslContext.newEngine(ch.alloc())));
    //添加 ChunkedWriteHandler 以处理作为 ChunkedInput 传入的数据
    pipeline.addLast(new ChunkedWriteHandler());
    //一旦连接建立，WriteStreamHandler 就开始写文件数据
    pipeline.addLast(new WriteStreamHandler());
  }

  public final class WriteStreamHandler extends ChannelInboundHandlerAdapter {

    /**
     * 当建立连接的时候，channelActive方法使用ChunkedStream写文件数据
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
      super.channelActive(ctx);
      ctx.writeAndFlush(new ChunkedStream(new FileInputStream(file)));
    }
  }
}
