package nia.chapter8;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import java.net.InetSocketAddress;

/**
 * @author mawenlong
 * @date 2018/09/21
 *
 * 引导和使用 ChannelInitializer
 */
public class BootstrapWithInitializer {

  /**
   * 代码清单 8-6 引导和使用 ChannelInitializer
   */
  public void bootstrap() throws InterruptedException {
    ServerBootstrap bootstrap = new ServerBootstrap();
    bootstrap.group(new NioEventLoopGroup(), new NioEventLoopGroup())
        .channel(NioServerSocketChannel.class)
        .childHandler(new ChannelInitializerImpl());
    ChannelFuture future = bootstrap.bind(new InetSocketAddress(8080));
    future.sync();
  }

  /**
   * 用以设置ChannelPipeline的自定义ChannelInitializerImpl实现
   */
  final class ChannelInitializerImpl extends ChannelInitializer<Channel> {

    @Override
    protected void initChannel(Channel ch) throws Exception {
      ChannelPipeline pipeline = ch.pipeline();
      pipeline.addLast(new HttpClientCodec());
      pipeline.addLast(new HttpObjectAggregator(Integer.MAX_VALUE));
    }
  }
}
