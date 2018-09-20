package nia.chapter7;

import java.util.Collections;
import java.util.List;

/**
 * @author mawenlong
 * @date 2018/09/20
 * describe: 事件循环的基本思想
 */
public class EventLoopExamples {
    /**
     * 代码清单 7-1 在事件循环中执行任务
     * */
    public static void executeTaskInEventLoop() {
        boolean terminated = true;
        while (!terminated){
            //阻塞，直到有事件已经就绪可被运行
            List<Runnable> readyEvents = blockUntilEventsReady();
            //循环遍历，并处理所有事件
            for(Runnable ev:readyEvents){
                ev.run();
            }
        }
    }
    private static final List<Runnable> blockUntilEventsReady() {
        return Collections.<Runnable>singletonList(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
