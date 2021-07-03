package com.pc.seckill.queue.delay.jvm;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.DelayQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 红包过期失效 延迟队列
 */
@Slf4j
public class RedPacketDelayQueue {

    public static void main(String[] args) throws Exception {
        DelayQueue<RedPacketMessage> queue = new DelayQueue<>();
        // 默认延迟3秒
        RedPacketMessage message = new RedPacketMessage(1);
        queue.add(message);
        // 延迟5秒
        message = new RedPacketMessage(2, 5);
        queue.add(message);
        // 延迟10秒
        message = new RedPacketMessage(3, 10);
        queue.add(message);
        ExecutorService executorService = Executors.newSingleThreadExecutor(r -> {
            Thread thread = new Thread(r);
            thread.setName("DelayWorker");
            thread.setDaemon(true);
            return thread;
        });
        log.info("开始执行调度线程...");
        executorService.execute(() -> {
            while (true) {
                System.out.println("111");
                try {
                    RedPacketMessage task = queue.take();
                    System.out.println("111");
                    log.info("延迟处理红包消息,{}", task.getDescription());
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            }
        });
        Thread.sleep(Integer.MAX_VALUE);
    }
}
