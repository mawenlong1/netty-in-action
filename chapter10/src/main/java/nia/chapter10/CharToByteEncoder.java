package nia.chapter10;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author mawenlong
 * @date 2018/09/22
 */
public class CharToByteEncoder extends
    MessageToByteEncoder<Character> {

  @Override
  public void encode(ChannelHandlerContext ctx, Character msg,
      ByteBuf out) throws Exception {
    out.writeChar(msg);
  }
}