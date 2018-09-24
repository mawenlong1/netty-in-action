package nia.chapter13;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author mawenlong
 * @date 2018/09/24
 *
 * extends SimpleChannelInboundHandler<LogEvent>
 */
public class LogEventHandler extends SimpleChannelInboundHandler<LogEvent> {

  @Override
  protected void channelRead0(ChannelHandlerContext ctx, LogEvent event) throws Exception {
    StringBuilder builder = new StringBuilder();
    builder.append(event.getReceivedTimestamp());
    builder.append(" [");
    builder.append(event.getSource().toString());
    builder.append("] [");
    builder.append(event.getLogfile());
    builder.append("] : ");
    builder.append(event.getMsg());
    System.out.println(builder.toString());
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    cause.printStackTrace();
    ctx.close();
  }
}
