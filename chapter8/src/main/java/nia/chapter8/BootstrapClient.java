package nia.chapter8;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import java.net.InetSocketAddress;

/**
 * @author mawenlong
 * @date 2018/09/21
 *
 * 引导一个客户端
 */
public class BootstrapClient {

  /**
   * 代码清单 8-1 引导一个客户端
   */
  public void bootstrap() {
    EventLoopGroup group = new NioEventLoopGroup();
    Bootstrap bootstrap = new Bootstrap();
    bootstrap.group(group)
        .channel(NioSocketChannel.class)
        .handler(new SimpleChannelInboundHandler<ByteBuf>() {
          @Override
          protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
            System.out.println("Received date");
          }
        });
    //连接到远程主机
    ChannelFuture future = bootstrap.connect(new InetSocketAddress("localhost", 8080));
    future.addListener(new ChannelFutureListener() {
      @Override
      public void operationComplete(ChannelFuture future) throws Exception {
        if (future.isSuccess()) {
          System.out.println("connection established");
        } else {
          System.out.println("connection attempt failed");
          future.cause().printStackTrace();
        }
      }
    });
  }

  public static void main(String[] args) {
    BootstrapClient client = new BootstrapClient();
    client.bootstrap();
  }

}
