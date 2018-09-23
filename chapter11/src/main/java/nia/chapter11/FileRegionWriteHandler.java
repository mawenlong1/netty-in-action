package nia.chapter11;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.DefaultFileRegion;
import io.netty.channel.FileRegion;
import io.netty.channel.socket.nio.NioSocketChannel;
import java.io.File;
import java.io.FileInputStream;

/**
 * @author mawenlong
 * @date 2018/09/23
 *
 * 使用 FileRegion 传输文件的内容
 */
public class FileRegionWriteHandler extends ChannelInboundHandlerAdapter {

  private static final Channel CHANNEL_FROM_SOMEWHERE = new NioSocketChannel();
  private static final File FILE_FROM_SOMEWHERE = new File("");

  @Override
  public void channelActive(ChannelHandlerContext ctx) throws Exception {
    File file = FILE_FROM_SOMEWHERE;
    Channel channel = CHANNEL_FROM_SOMEWHERE;

//    创建一个FileInputStream
    FileInputStream in = new FileInputStream(file);
//    以该文件的完整长度创建一个新的DefaultFileRegion
    FileRegion region = new DefaultFileRegion(in.getChannel(), 0, file.length());
//    发送该DefaultFileRegion，并注册一个ChannelFutureListener
    channel.writeAndFlush(region)
        .addListener(new ChannelFutureListener() {
          @Override
          public void operationComplete(ChannelFuture future) throws Exception {
            if (!future.isSuccess()) {
              Throwable cause = future.cause();
//              处理失败
            }
          }
        });

  }
}
