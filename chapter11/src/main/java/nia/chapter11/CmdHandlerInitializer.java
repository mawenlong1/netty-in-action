package nia.chapter11;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.LineBasedFrameDecoder;

/**
 * @author mawenlong
 * @date 2018/09/23
 */
public class CmdHandlerInitializer extends ChannelInitializer<Channel> {

  private static final byte SPACE = (byte) ' ';

  @Override
  protected void initChannel(Channel ch) throws Exception {
    ChannelPipeline pipeline = ch.pipeline();
    pipeline.addLast(new CmdDecoder(64 * 1024));
    pipeline.addLast(new CmdHandler());
  }

  public static final class Cmd {

    private final ByteBuf name;
    private final ByteBuf args;

    public Cmd(ByteBuf name, ByteBuf args) {
      this.name = name;
      this.args = args;
    }

    public ByteBuf name() {
      return name;
    }

    public ByteBuf args() {
      return args;
    }
  }

  public static final class CmdDecoder extends LineBasedFrameDecoder {

    public CmdDecoder(int maxLength) {
      super(maxLength);
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf buffer) throws Exception {
//      从ByteBuf中提取有行尾符序列分隔的帧
      ByteBuf frame = (ByteBuf) super.decode(ctx, buffer);
      if (frame == null) {
        return null;
      }
//    查找第一个空格的索引。前面是命令名称后面是参数
      int index = frame.indexOf(frame.readerIndex(), frame.writerIndex(), SPACE);
      return new Cmd(frame.slice(frame.readerIndex(), index),
          frame.slice(index + 1, frame.writerIndex()));
    }
  }

  public static final class CmdHandler extends SimpleChannelInboundHandler<Cmd> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Cmd msg) throws Exception {
//      处理传入的Cmd 对象
    }
  }
}
