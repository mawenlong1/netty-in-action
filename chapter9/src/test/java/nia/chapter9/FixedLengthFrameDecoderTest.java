package nia.chapter9;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.Test;

/**
 * @author mawenlong
 * @date 2018/09/22
 */
public class FixedLengthFrameDecoderTest {

  @Test
  public void testFramesDecoded() {
    ByteBuf buf = Unpooled.buffer();
    for (int i = 0; i < 9; i++) {
      buf.writeByte(i);
    }
    ByteBuf input = buf.duplicate();
    EmbeddedChannel channel = new EmbeddedChannel(new FixedLengthFrameDecoder(3));
//    将数据写入EmbeddedChannel
    assertTrue(channel.writeInbound(input.retain()));
//    标记完成Channel
    assertTrue(channel.finish());
//    读取所有生成的消息，并且验证是否有3帧（切片），其中每帧（切片）都为3帧
    ByteBuf read = (ByteBuf) channel.readInbound();
    assertEquals(buf.readSlice(3), read);
    read.release();

    read = (ByteBuf) channel.readInbound();
    assertEquals(buf.readSlice(3), read);
    read.release();

    read = (ByteBuf) channel.readInbound();
    assertEquals(buf.readSlice(3), read);
    read.release();

    assertNull(channel.readInbound());
    buf.release();
  }

  @Test
  public void testFramesDecoded2() {
    ByteBuf buf = Unpooled.buffer();
    for (int i = 0; i < 9; i++) {
      buf.writeByte(i);
    }
    ByteBuf input = buf.duplicate();
    EmbeddedChannel channel = new EmbeddedChannel(new FixedLengthFrameDecoder(3));
//    返回false，因为没有完整的的可读取的帧
    assertFalse(channel.writeInbound(input.readBytes(2)));
    assertTrue(channel.writeInbound(input.readBytes(7)));

    assertTrue(channel.finish());
    ByteBuf read = (ByteBuf) channel.readInbound();
    assertEquals(buf.readSlice(3), read);
    read.release();

    read = (ByteBuf) channel.readInbound();
    assertEquals(buf.readSlice(3), read);
    read.release();

    read = (ByteBuf) channel.readInbound();
    assertEquals(buf.readSlice(3), read);
    read.release();

    assertNull(channel.readInbound());
    buf.release();

  }
}
