package nia.chapter9;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.TooLongFrameException;
import java.util.List;

/**
 * @author mawenlong
 * @date 2018/09/22
 *
 * 扩展 ByteToMessageDecoder以将入站字节解码为消息
 */
public class FrameChunkDecoder extends ByteToMessageDecoder {

  private final int maxFrameSize;

  public FrameChunkDecoder(int maxFrameSize) {
    this.maxFrameSize = maxFrameSize;
  }

  @Override
  protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
    int readableBytes = in.readableBytes();
    if (readableBytes > maxFrameSize) {
//       大于最大帧，丢弃
      in.clear();
      throw new TooLongFrameException();
    }
//    否则，从ByteBuf中读取一个新帧
    ByteBuf buf = in.readBytes(readableBytes);
    out.add(buf);
  }
}
