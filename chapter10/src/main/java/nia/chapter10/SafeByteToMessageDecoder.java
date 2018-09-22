package nia.chapter10;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.TooLongFrameException;
import java.util.List;

/**
 * @author mawenlong
 * @date 2018/09/22
 *
 * 扩展 ByteToMessageDecoder 以将字节解码为消息
 */
public class SafeByteToMessageDecoder extends ByteToMessageDecoder {

  private static final int MAX_FRAME_SIZE = 1024;

  @Override
  protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
    int readable = in.readableBytes();
    if (readable > MAX_FRAME_SIZE) {
//      跳过可读字节并抛出异常
      in.skipBytes(readable);
      throw new TooLongFrameException("frame too big!");
    }
  }
}
