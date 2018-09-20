package nia.chapter6;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.util.ReferenceCountUtil;

/**
 * @author mawenlong
 * @date 2018/09/20
 * describe:
 */
public class DiscardOutboundHandler extends ChannelOutboundHandlerAdapter {
    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        //释放资源
        ReferenceCountUtil.release(msg);
        //通知ChannelPromise数据已被处理
        promise.setSuccess();
    }
}
