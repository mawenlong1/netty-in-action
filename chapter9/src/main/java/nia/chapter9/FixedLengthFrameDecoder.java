package nia.chapter9;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import java.util.List;

/**
 * @author mawenlong
 * @date 2018/09/22
 *
 * 扩展 ByteToMessageDecoder 以处理入站字节，并将它们解码为消息
 */
public class FixedLengthFrameDecoder extends ByteToMessageDecoder {

  private final int framelLenght;

  public FixedLengthFrameDecoder(int framelLenght) {
    if (framelLenght <= 0) {
      throw new IllegalArgumentException("frameLenth must be a positive integer:" + framelLenght);
    }
    this.framelLenght = framelLenght;
  }

  @Override
  protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
//    检查是否有足够的字节可以被读取，以生成下一个帧
    while (in.readableBytes() >= framelLenght) {
//      从ByteBuf中读取一个新帧
      ByteBuf buf = in.readBytes(framelLenght);
//      将该帧添加到已被解码的消息列表
      out.add(buf);
    }
  }
}
