package nia.chapter11;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;

/**
 * @author mawenlong
 * @date 2018/09/22
 *
 * 自动聚合 HTTP 的消息片段
 */
public class HttpAggregatorInitializer extends ChannelInitializer<Channel> {

  private final boolean isClient;

  public HttpAggregatorInitializer(boolean isClient) {
    this.isClient = isClient;
  }


  @Override
  protected void initChannel(Channel ch) throws Exception {
    ChannelPipeline pipeline = ch.pipeline();
    if (isClient) {
      pipeline.addLast("codec", new HttpClientCodec());
    } else {
      pipeline.addLast("codec", new HttpServerCodec());
    }
//   将最大的消息大小为 512 KB 的 HttpObjectAggregator 添加到 ChannelPipeline
    pipeline.addLast("aggregator", new HttpObjectAggregator(512 * 1024));
  }
}
