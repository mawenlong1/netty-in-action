package nia.chapter6;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author mawenlong
 * @date 2018/09/20
 * describe: @Sharable注解的错误使用，count并发访问导致问题
 */
@ChannelHandler.Sharable
public class UnsharableHandler extends ChannelInboundHandlerAdapter {
    private int count;
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        //将 count 字段的值加 1
        count++;
        //记录方法调用，并转发给下一个ChannelHandler
        System.out.println("inboundBufferUpdated(...) called the "
                + count + " time");
        ctx.fireChannelRead(msg);
    }
}