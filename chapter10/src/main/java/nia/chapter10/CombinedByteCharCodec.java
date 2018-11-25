package nia.chapter10;

import io.netty.channel.CombinedChannelDuplexHandler;

/**
 * @author mawenlong
 * @date 2018/09/22
 *
 * 编解码器 通过该解码器和编码器实现参数化 CombinedByteCharCodec
 */
public class CombinedByteCharCodec extends
        CombinedChannelDuplexHandler<ByteToCharDecoder, CharToByteEncoder> {

    public CombinedByteCharCodec() {
        //将委托实例传递给父类
        super(new ByteToCharDecoder(), new CharToByteEncoder());
    }
}
