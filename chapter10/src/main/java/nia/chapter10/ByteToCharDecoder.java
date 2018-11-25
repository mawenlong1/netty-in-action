package nia.chapter10;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * @author mawenlong
 * @date 2018/09/22
 */
public class ByteToCharDecoder extends ByteToMessageDecoder {

    @Override
    public void decode(ChannelHandlerContext ctx, ByteBuf in,
                       List<Object> out) throws Exception {
        if (in.readableBytes() >= 2) {
            out.add(in.readChar());
        }
    }
}