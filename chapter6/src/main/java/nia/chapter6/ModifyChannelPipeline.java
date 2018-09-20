package nia.chapter6;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelPipeline;

import static io.netty.channel.DummyChannelPipeline.DUMMY_INSTANCE;

/**
 * @author mawenlong
 * @date 2018/09/20
 * describe:
 */
public class ModifyChannelPipeline {
    private static final ChannelPipeline CHANNEL_PIPELINE_FROM_SOMEWHERE = DUMMY_INSTANCE;

    /**
     * 代码清单 6-5 修改 ChannelPipeline
     */
    public static void modifyPipeline() {
        ChannelPipeline pipeline = CHANNEL_PIPELINE_FROM_SOMEWHERE;
        FirstHandler firstHandler = new FirstHandler();
        //添加handler
        pipeline.addLast("handler1", firstHandler);
        //将handler2添加到ChannelPipeline的第一个槽中。它将被放置在已有的handler1之前
        pipeline.addFirst("handler2", new SecondHandler());
        //添加到最后一个槽中
        pipeline.addLast("handler3", new ThirdHandler());

        //移除handler
        pipeline.remove("handler3");
        pipeline.remove(firstHandler);
        //替换
        pipeline.replace("handler2", "handler4", new FourthHandler());
    }

    private static final class FirstHandler
            extends ChannelHandlerAdapter {

    }

    private static final class SecondHandler
            extends ChannelHandlerAdapter {

    }

    private static final class ThirdHandler
            extends ChannelHandlerAdapter {

    }

    private static final class FourthHandler
            extends ChannelHandlerAdapter {

    }
}
