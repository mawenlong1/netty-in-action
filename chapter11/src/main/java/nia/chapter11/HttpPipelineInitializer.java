package nia.chapter11;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpRequestEncoder;
import io.netty.handler.codec.http.HttpResponseDecoder;

/**
 * @author mawenlong
 * @date 2018/09/22
 *
 * 添加 HTTP 支持
 */
public class HttpPipelineInitializer extends ChannelInitializer<Channel> {

  private final boolean client;

  public HttpPipelineInitializer(boolean client) {
    this.client = client;
  }

  @Override
  protected void initChannel(Channel ch) throws Exception {
    ChannelPipeline pipeline = ch.pipeline();
    if (client) {
//      如果是客户端，则添加 HttpResponseDecode解码 以处理来自服务器的响应
      pipeline.addLast("decode", new HttpResponseDecoder());
//      如果是客户端，则添加 HttpRequestEncoder编码 以向服务器发送请求
      pipeline.addLast("encode", new HttpRequestEncoder());
    } else {
//      如果是服务器，则添加 HttpRequestDecoder解码 以接收来自客户端的请求
      pipeline.addLast("decode", new HttpRequestEncoder());
//      如果是服务器，则添加 HttpResponseEncoder编码 以向客户端发送响应
      pipeline.addLast("encode", new HttpResponseDecoder());
    }
  }
}
