package nia.chapter11;

import com.google.protobuf.MessageLite;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import java.io.Serializable;

/**
 * @author mawenlong
 * @date 2018/09/23
 *
 * 使用 protobuf序列化
 */
public class ProtoBufInitializer extends ChannelInitializer<Channel> {

  private final MessageLite lite;

  public ProtoBufInitializer(MessageLite lite) {
    this.lite = lite;
  }

  @Override
  protected void initChannel(Channel ch) throws Exception {
    ChannelPipeline pipeline = ch.pipeline();
    //添加 ProtobufVarint32FrameDecoder 以分隔帧
    pipeline.addLast(new ProtobufVarint32FrameDecoder());
    //添加 ProtobufEncoder 以处理消息的编码
    pipeline.addLast(new ProtobufEncoder());
    //添加 ProtobufDecoder 以解码消息
    pipeline.addLast(new ProtobufDecoder(lite));
    //添加 ObjectHandler 以处理解码消息
    pipeline.addLast(new ObjectHandler());
  }

  public static final class ObjectHandler extends SimpleChannelInboundHandler<Serializable> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Serializable msg) throws Exception {

    }
  }
}
