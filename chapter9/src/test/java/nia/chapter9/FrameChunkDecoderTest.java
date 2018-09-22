package nia.chapter9;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.TooLongFrameException;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author mawenlong
 * @date 2018/09/22
 */
public class FrameChunkDecoderTest {

  @Test
  public void testFramesDecoded() {
    ByteBuf buf = Unpooled.buffer();
    for (int i = 0; i < 9; i++) {
      buf.writeByte(i);
    }
    ByteBuf input = buf.duplicate();

    EmbeddedChannel channel = new EmbeddedChannel(
        new FrameChunkDecoder(3));

    //向它写入 2 字节，并断言它们将会产生一个新帧
    assertTrue(channel.writeInbound(input.readBytes(2)));
    try {
//      写入一个大于4字节的帧并捕获异常
      channel.writeInbound(input.readBytes(4));
      Assert.fail();
    } catch (TooLongFrameException e) {

    }
    assertTrue(channel.writeInbound(input.readBytes(3)));
    assertTrue(channel.finish());

//    读取数据
    ByteBuf read = (ByteBuf) channel.readInbound();
    assertEquals(buf.readSlice(2), read);
    read.release();

    read = (ByteBuf) channel.readInbound();
    assertEquals(buf.skipBytes(4).readSlice(3), read);
    read.release();

    buf.release();
  }
}
