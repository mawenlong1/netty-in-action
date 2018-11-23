package nia.chapter2.echoserver;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * @author mawenlong
 * @date 2018/11/23
 */
public class Test {
    public static void main(String[] args) {
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(10);
        ScheduledFuture<?> future = executor.schedule(() -> System.out.println("3 seconds"), 3, TimeUnit.SECONDS);
        executor.shutdown();
    }
}
