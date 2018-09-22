package nia.chapter9;

import static org.junit.Assert.assertEquals;
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
public class AbsIntegerEncoderTest {

  @Test
  public void testEncoded() {
    ByteBuf buf = Unpooled.buffer();
    for (int i = 0; i < 10; i++) {
      buf.writeInt(i * -1);
    }
    EmbeddedChannel channel = new EmbeddedChannel(new AbsIntegerEncoder());
    assertTrue(channel.writeOutbound(buf));
    assertTrue(channel.finish());

    for (int i = 0; i < 10; i++) {
      assertEquals(i, channel.readOutbound());
    }
    assertNull(channel.readOutbound());

  }
}
