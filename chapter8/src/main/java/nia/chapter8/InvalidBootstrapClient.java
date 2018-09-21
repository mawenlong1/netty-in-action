package nia.chapter8;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.oio.OioSocketChannel;
import java.net.InetSocketAddress;

/**
 * @author mawenlong
 * @date 2018/09/21
 *
 * 不兼容channel和eventloopgroup
 */
public class InvalidBootstrapClient {

  /**
   * 代码清单 8-3 不兼容的 Channel 和 EventLoopGroup
   */
  public void bootstrap() {
    EventLoopGroup group = new NioEventLoopGroup();
    Bootstrap bootstrap = new Bootstrap();
    bootstrap.group(group)
        .channel(OioSocketChannel.class)
        .handler(new SimpleChannelInboundHandler<ByteBuf>() {
          @Override
          protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
            System.out.println("Received date");
          }
        });
    ChannelFuture future = bootstrap.connect(new InetSocketAddress("www.baidu.com", 80));
    future.syncUninterruptibly();
  }

  public static void main(String[] args) {
    InvalidBootstrapClient client = new InvalidBootstrapClient();
    client.bootstrap();
  }
}
