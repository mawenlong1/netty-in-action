package nia.chapter10;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import java.util.List;

/**
 * @author mawenlong
 * @date 2018/09/22
 *
 * 扩展 ReplayingDecoder<Void> 以将字节解码为消息
 */
public class ToIntegerDecoder2 extends ReplayingDecoder<Void> {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        out.add(in.readInt());
    }
}
