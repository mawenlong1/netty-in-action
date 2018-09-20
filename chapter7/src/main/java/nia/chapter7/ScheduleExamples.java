package nia.chapter7;

import io.netty.channel.Channel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * @author mawenlong
 * @date 2018/09/20
 * describe:
 */
public class ScheduleExamples {
    private static final Channel CHANNEL_FROM_SOMEWHERE = new NioSocketChannel();

    /**
     * 代码清单 7-2 使用 ScheduledExecutorService 调度任务
     */
    public static void schedule() {
        //创建一个其线程池具有10个线程的ScheduledExecutorService
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(10);
        ScheduledFuture<?> future = executorService.schedule(new Runnable() {
            @Override
            public void run() {
                System.out.println("60 secondes later");
            }
        }, 60, TimeUnit.SECONDS);

    }

    /**
     * 代码清单 7-3 使用 EventLoop 调度任务
     */
    public static void scheduleViaEventLoop() {
        Channel ch = CHANNEL_FROM_SOMEWHERE;

        ScheduledFuture<?> future = ch.eventLoop().schedule(
                //创建一个 Runnable以供调度稍后执行
                new Runnable() {
                    @Override
                    public void run() {
                        //要执行的代码
                        System.out.println("60 seconds later");
                    }
                    //调度任务在从现在开始的 60 秒之后执行
                }, 60, TimeUnit.SECONDS);
    }

    /**
     * 代码清单 7-4 使用 EventLoop 调度周期性的任务
     */
    public static void scheduleFixedViaEventLoop() {
        Channel ch = CHANNEL_FROM_SOMEWHERE;
        ScheduledFuture<?> future = ch.eventLoop().scheduleAtFixedRate(
                //创建一个 Runnable，以供调度稍后执行
                new Runnable() {
                    @Override
                    public void run() {
                        //这将一直运行，直到 ScheduledFuture 被取消
                        System.out.println("Run every 60 seconds");
                    }
                    //调度在 60 秒之后，并且以后每间隔 60 秒运行
                }, 60, 60, TimeUnit.SECONDS);
    }

    /**
     * 代码清单 7-5 使用 ScheduledFuture 取消任务
     * */
    public static void cancelingTaskUsingScheduledFuture(){
        Channel ch = CHANNEL_FROM_SOMEWHERE;
        //调度任务，并获得所返回的ScheduledFuture
        ScheduledFuture<?> future = ch.eventLoop().scheduleAtFixedRate(
                new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("Run every 60 seconds");
                    }
                }, 60, 60, TimeUnit.SECONDS);
        //其他代码
        boolean mayInterruptIfRunning = false;
        //取消该任务，防止它再次运行
        future.cancel(mayInterruptIfRunning);
    }
    public static void main(String[] args) {
        scheduleFixedViaEventLoop();
    }
}
