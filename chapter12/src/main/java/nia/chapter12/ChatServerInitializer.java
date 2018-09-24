package nia.chapter12;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * @author mawenlong
 * @date 2018/09/24
 */
public class ChatServerInitializer extends ChannelInitializer<Channel> {

  private final ChannelGroup group;

  public ChatServerInitializer(ChannelGroup group) {
    this.group = group;
  }

  /**
   * 将所有需要的 ChannelHandler 添加到 ChannelPipelines 中
   */
  @Override
  protected void initChannel(Channel ch) throws Exception {
    ChannelPipeline pipeline = ch.pipeline();
    pipeline.addLast(new HttpServerCodec())
        .addLast(new ChunkedWriteHandler())
        .addLast(new HttpObjectAggregator(64 * 1024))
        .addLast(new HttpRequestHandler("/ws"))
        .addLast(new WebSocketServerProtocolHandler("/ws"))
        .addLast(new TextWebSocketFrameHandler(group));
  }
}
