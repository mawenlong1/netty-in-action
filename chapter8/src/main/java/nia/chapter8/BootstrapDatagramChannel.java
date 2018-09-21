package nia.chapter8;


import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.oio.OioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.oio.OioDatagramChannel;
import java.net.InetSocketAddress;

/**
 * @author mawenlong
 * @date 2018/09/21
 *
 * 使用 Bootstrap 和 DatagramChannel 无连接
 */
public class BootstrapDatagramChannel {

  /**
   * 代码清单 8-8 使用 Bootstrap 和 DatagramChannel
   */
  public void bootstrap() {
    Bootstrap bootstrap = new Bootstrap();
    bootstrap.group(new OioEventLoopGroup())
        .channel(OioDatagramChannel.class)
        .handler(new SimpleChannelInboundHandler<DatagramPacket>() {
          @Override
          protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket msg)
              throws Exception {
//            do something
          }
        });
//    调用bind方法，因为协议是无连接
    ChannelFuture future = bootstrap.bind(new InetSocketAddress(0));
    future.addListener(new ChannelFutureListener() {
      @Override
      public void operationComplete(ChannelFuture channelFuture) throws Exception {
        if (channelFuture.isSuccess()) {
          System.out.println("Channel bound");
        } else {
          System.err.println("bind attempt failed");
          channelFuture.cause().printStackTrace();
        }
      }
    });
  }
}
