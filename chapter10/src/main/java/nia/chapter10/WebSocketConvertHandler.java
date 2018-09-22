package nia.chapter10;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.ContinuationWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PingWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PongWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import java.util.List;
import nia.chapter10.WebSocketConvertHandler.MyWebSocketFrame;
import nia.chapter10.WebSocketConvertHandler.MyWebSocketFrame.FrameType;

/**
 * @author mawenlong
 * @date 2018/09/22
 *
 * 使用 MessageToMessageCodec
 */
public class WebSocketConvertHandler extends
    MessageToMessageCodec<WebSocketFrame, MyWebSocketFrame> {


  @Override
  protected void encode(ChannelHandlerContext ctx, MyWebSocketFrame msg, List<Object> out)
      throws Exception {
    ByteBuf payload = msg.getData().duplicate().retain();
//    实例化一个指定子类型的 WebSocketFrame
    switch (msg.getType()) {
      case BINARY:
        out.add(new BinaryWebSocketFrame(payload));
        break;
      case TEXT:
        out.add(new TextWebSocketFrame(payload));
        break;
      case CLOSE:
        out.add(new CloseWebSocketFrame(true, 0, payload));
        break;
      case CONTINUATION:
        out.add(new ContinuationWebSocketFrame(payload));
        break;
      case PONG:
        out.add(new PongWebSocketFrame(payload));
        break;
      case PING:
        out.add(new PingWebSocketFrame(payload));
        break;
      default:
        throw new IllegalStateException("Unsuported websocket msg" + msg);
    }
  }

  /**
   * 将 WebSocketFrame 解码为 MyWebSocketFrame，并设置 FrameType
   */
  @Override
  protected void decode(ChannelHandlerContext ctx, WebSocketFrame msg, List<Object> out)
      throws Exception {
    ByteBuf payload = msg.content().duplicate().retain();
    if (msg instanceof BinaryWebSocketFrame) {
      out.add(new MyWebSocketFrame(FrameType.BINARY, payload));
    } else if (msg instanceof CloseWebSocketFrame) {
      out.add(new MyWebSocketFrame(
          MyWebSocketFrame.FrameType.CLOSE, payload));
    } else if (msg instanceof PingWebSocketFrame) {
      out.add(new MyWebSocketFrame(
          MyWebSocketFrame.FrameType.PING, payload));
    } else if (msg instanceof PongWebSocketFrame) {
      out.add(new MyWebSocketFrame(
          MyWebSocketFrame.FrameType.PONG, payload));
    } else if (msg instanceof TextWebSocketFrame) {
      out.add(new MyWebSocketFrame(
          MyWebSocketFrame.FrameType.TEXT, payload));
    } else if (msg instanceof ContinuationWebSocketFrame) {
      out.add(new MyWebSocketFrame(
          MyWebSocketFrame.FrameType.CONTINUATION, payload));
    } else {
      throw new IllegalStateException(
          "Unsupported websocket msg " + msg);
    }
  }

  /**
   * 声明WebSocketConvertHandler所使用的OUTBOUND_IN类型
   */
  public static final class MyWebSocketFrame {

    //定义拥有被包装的有效负载WebSocketFrame的类型
    public enum FrameType {
      BINARY,
      CLOSE,
      PING,
      PONG,
      TEXT,
      CONTINUATION
    }

    private final FrameType type;
    private final ByteBuf data;

    public MyWebSocketFrame(FrameType type, ByteBuf data) {
      this.type = type;
      this.data = data;
    }

    public FrameType getType() {
      return type;
    }

    public ByteBuf getData() {
      return data;
    }
  }
}
