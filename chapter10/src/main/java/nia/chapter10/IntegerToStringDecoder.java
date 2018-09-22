package nia.chapter10;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import java.util.List;

/**
 * @author mawenlong
 * @date 2018/09/22
 *
 * 扩展了 MessageToMessageEncoder 将Integer转为String
 */
public class IntegerToStringDecoder extends MessageToMessageDecoder<Integer> {

  @Override
  protected void decode(ChannelHandlerContext ctx, Integer msg, List<Object> out) throws Exception {
    out.add(String.valueOf(msg));
  }
}
