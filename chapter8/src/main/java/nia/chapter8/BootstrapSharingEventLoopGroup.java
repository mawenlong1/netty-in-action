package nia.chapter8;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import java.net.InetSocketAddress;

/**
 * @author mawenlong
 * @date 2018/09/21
 *
 * 引导服务器，即充当服务器也充当客户端
 */
public class BootstrapSharingEventLoopGroup {

  /**
   * 代码清单 8-5 引导服务器
   */
  public void bootstrap() {
    final ServerBootstrap bootstrap = new ServerBootstrap();
//    设置EventLoopGroup，其将提供用以处理Channel事件的EventLoop
    bootstrap.group(new NioEventLoopGroup(), new NioEventLoopGroup())
        .channel(NioServerSocketChannel.class)
        .childHandler(new SimpleChannelInboundHandler<ByteBuf>() {
          ChannelFuture connectFuture;

          @Override
          public void channelActive(ChannelHandlerContext ctx) throws Exception {
//            创建Bootstrap类的实例以连接到远程主机
            Bootstrap bootstrap1 = new Bootstrap();
            bootstrap1.channel(NioServerSocketChannel.class)
                .handler(new SimpleChannelInboundHandler<ByteBuf>() {
                  @Override
                  protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg)
                      throws Exception {
                    System.out.println("revcvied data");
                  }
                });
//            使用与分配给已接受的子Channel相同的EventLoop
            bootstrap1.group(ctx.channel().eventLoop());
            connectFuture = bootstrap1.connect(new InetSocketAddress("www.baidu.com", 80));
          }

          @Override
          protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
            if (connectFuture.isDone()) {
//              对数据进行操作
            }
          }
        });
    ChannelFuture future = bootstrap.bind(new InetSocketAddress(8080));
    future.addListener(new ChannelFutureListener() {
      @Override
      public void operationComplete(ChannelFuture channelFuture) throws Exception {
        if (channelFuture.isSuccess()) {
          System.out.println("Server bound");
        } else {
          System.err.println("bind attempt failed");
          channelFuture.cause().printStackTrace();
        }
      }
    });
  }

  public static void main(String[] args) {
    new BootstrapSharingEventLoopGroup().bootstrap();
  }
}
