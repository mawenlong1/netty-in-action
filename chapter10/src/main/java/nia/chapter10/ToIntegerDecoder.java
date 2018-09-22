package nia.chapter10;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import java.util.List;

/**
 * @author mawenlong
 * @date 2018/09/22
 *
 * 扩展ByteToMessageDecoder类，以将字节解码为特定的格式
 */
public class ToIntegerDecoder extends ByteToMessageDecoder {

  @Override
  protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
    if (in.readableBytes() >= 4) {
      out.add(in.readInt());
    }
  }
}
