package nia.chapter8;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.Future;
import java.net.InetSocketAddress;

/**
 * @author mawenlong
 * @date 2018/09/21
 *
 * 优雅的关闭
 */
public class GracefulShutdown {

  /**
   * 代码清单 8-9 优雅关闭
   */
  public void bootstrap() {
    EventLoopGroup group = new NioEventLoopGroup();
    Bootstrap bootstrap = new Bootstrap();
    bootstrap.group(group)
        .channel(NioSocketChannel.class)
        .handler(
            new SimpleChannelInboundHandler<ByteBuf>() {
              @Override
              protected void channelRead0(
                  ChannelHandlerContext channelHandlerContext,
                  ByteBuf byteBuf) throws Exception {
                System.out.println("Received data");
              }
            }
        );
    bootstrap.connect(new InetSocketAddress("www.manning.com", 80)).syncUninterruptibly();
    //...do something
    //shutdownGracefully()方法将释放所有的资源，并且关闭所有的当前正在使用中的 Channel
    Future<?> future = group.shutdownGracefully();
    // 阻塞直到关闭
    future.syncUninterruptibly();
  }
}
