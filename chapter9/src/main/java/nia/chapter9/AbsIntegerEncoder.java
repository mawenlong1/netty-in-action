package nia.chapter9;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import java.util.List;

/**
 * @author mawenlong
 * @date 2018/09/22
 *
 * 扩展 MessageToMessageEncoder 以将一个消息编码为另外一种格式
 */
public class AbsIntegerEncoder extends MessageToMessageEncoder<ByteBuf> {

  @Override
  protected void encode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
    while (msg.readableBytes() >= 4) {
//      读取整数并计算绝对值
      int value = Math.abs(msg.readInt());
      out.add(value);
    }
  }
}
