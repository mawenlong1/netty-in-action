package io.netty.channel;

/**
 * @author mawenlong
 * @date 2018/09/20
 * describe:
 */
public class DummyChannelPipeline extends DefaultChannelPipeline {
    public static final ChannelPipeline DUMMY_INSTANCE = new DummyChannelPipeline(null);

    public DummyChannelPipeline(Channel channel) {
        super(channel);
    }
}
