package nia.chapter8;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOption;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;
import java.net.InetSocketAddress;

/**
 * @author mawenlong
 * @date 2018/09/21
 *
 * 使用属性值
 */
public class BootstrapClientWithOptionsAndAttrs {

  /**
   * 代码清单 8-7 使用属性值
   */
  public void bootstrap() {
    final AttributeKey<Integer> id = AttributeKey.newInstance("ID");
    Bootstrap bootstrap = new Bootstrap();
    bootstrap.group(new NioEventLoopGroup())
        .channel(NioSocketChannel.class)
        .handler(new SimpleChannelInboundHandler<ByteBuf>() {
          @Override
          public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
            Integer idValue = ctx.channel().attr(id).get();
//           操作idValue
          }

          @Override
          protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
            System.out.println("received data");
          }
        });
    bootstrap.option(ChannelOption.SO_KEEPALIVE, true)
        .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000);
//    存储该id的属性
    bootstrap.attr(id, 123456);
    ChannelFuture future = bootstrap.connect(new InetSocketAddress("www.baidu.com", 80));
    future.syncUninterruptibly();
  }
}
