package mwl.nia.chapter1;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author mawenlong
 * @date 2018/09/17
 * describe: 代码清单 1-2 被回调触发的 ChannelHandler
 */
public class ConnectHandler extends ChannelInboundHandlerAdapter {
    /**
     * 当一个新的连接已经被建立时，channelActive(ChannelHandlerContext)将会被调用
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(
                "Client " + ctx.channel().remoteAddress() + " connected");
    }
}
