package com.pc.seckill.controller;

import com.pc.seckill.common.entity.Result;
import com.pc.seckill.common.entity.Seckill;
import com.pc.seckill.common.entity.SuccessKilled;
import com.pc.seckill.common.exception.RrException;
import com.pc.seckill.queue.disruptor.DisruptorUtil;
import com.pc.seckill.queue.disruptor.SeckillEvent;
import com.pc.seckill.queue.jvm.SeckillQueue;
import com.pc.seckill.service.ISeckillService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Api(tags = "秒杀")
@RestController
@RequestMapping("/seckills")
@Slf4j
public class SeckillGoodsController {

    private static int corePoolSize = Runtime.getRuntime().availableProcessors();
    /**
     * 创建线程池  调整队列数 拒绝服务
     */
    private static ThreadPoolExecutor executor = new ThreadPoolExecutor(corePoolSize, corePoolSize + 1, 10l, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(1000));

    @Autowired
    private ISeckillService seckillService;

    @ApiOperation(value = "秒杀一(最low实现)", nickname = "爪哇笔记")
    @PostMapping("/start")
    public Result start(long seckillId) {
        int skillNum = 10;
        final CountDownLatch latch = new CountDownLatch(skillNum);
        seckillService.deleteSeckill(seckillId);
        final long killId = seckillId;
        log.info("开始秒杀一(会出现超卖)");
        /**
         * 开启新线程之前，将RequestAttributes对象设置为子线程共享
         * 这里仅仅是为了测试，否则 IPUtils 中获取不到 request 对象
         * 用到限流注解的测试用例，都需要加一下两行代码
         */
        ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        RequestContextHolder.setRequestAttributes(sra, true);
        for (int i = 0; i < skillNum; i++) {
            final long userId = i;
            Runnable task = () -> {
                /**
                 * 坏蛋说 抛异常影响最终效果
                 */
                try {
                    Result result = seckillService.startSeckil(killId, userId);
                    if (result != null) {
                        log.info("用户:{}{}", userId, result.get("msg"));
                    } else {
                        log.info("用户:{}{}", userId, "哎呦喂，人也太多了，请稍后！");
                    }
                } catch (RrException e) {
                    log.error("哎呀报错了{}", e.getMsg());
                }
                latch.countDown();
            };
            executor.execute(task);
        }
        try {
            latch.await();// 等待所有人任务结束
            Long seckillCount = seckillService.getSeckillCount(seckillId);
            log.info("一共秒杀出{}件商品", seckillCount);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return Result.ok();
    }

    @ApiOperation(value = "秒杀二(程序锁)", nickname = "科帮网")
    @PostMapping("/startLock")
    public Result startLock(long seckillId) {
        int skillNum = 1000;
        final CountDownLatch latch = new CountDownLatch(skillNum);
        seckillService.deleteSeckill(seckillId);
        final long killId = seckillId;
        log.info("开始秒杀二(正常)");
        for (int i = 0; i < 1000; i++) {
            final long userId = i;
            Runnable task = () -> {
                Result result = seckillService.startSeckilLock(killId, userId);
                log.info("用户:{}{}", userId, result.get("msg"));
                latch.countDown();
            };
            executor.execute(task);
        }
        try {
            latch.await();// 等待所有人任务结束
            Long seckillCount = seckillService.getSeckillCount(seckillId);
            log.info("一共秒杀出{}件商品", seckillCount);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return Result.ok();
    }

    @ApiOperation(value = "秒杀三(AOP程序锁)", nickname = "科帮网")
    @PostMapping("/startAopLock")
    public Result startAopLock(@RequestBody Seckill req) {
        int skillNum = 1000;
        final CountDownLatch latch = new CountDownLatch(skillNum);
        seckillService.deleteSeckill(req.getSeckillId());
        log.info("开始秒杀三(正常)");
        Runnable task = () -> {
            Result result = seckillService.startSeckilAopLock(req.getSeckillId(), req.getUserId());
            log.info("用户:{}{}", req.getUserId(), result.get("msg"));
            latch.countDown();
        };
        executor.execute(task);
        /*try {
            latch.await();// 等待所有人任务结束
            Long seckillCount = seckillService.getSeckillCount(req.getSeckillId());
            log.info("一共秒杀出{}件商品", seckillCount);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
        return Result.ok();
    }

    @ApiOperation(value = "秒杀四(数据库悲观锁)", nickname = "科帮网")
    @PostMapping("/startDBPCC_ONE")
    public Result startDBPCC_ONE(long seckillId) {
        int skillNum = 1000;
        final CountDownLatch latch = new CountDownLatch(skillNum);
        seckillService.deleteSeckill(seckillId);
        final long killId = seckillId;
        log.info("开始秒杀四(正常)");
        for (int i = 0; i < 1000; i++) {
            final long userId = i;
            Runnable task = () -> {
                Result result = seckillService.startSeckilDBPCC_ONE(killId, userId);
                log.info("用户:{}{}", userId, result.get("msg"));
                latch.countDown();
            };
            executor.execute(task);
        }
        try {
            latch.await();// 等待所有人任务结束
            Long seckillCount = seckillService.getSeckillCount(seckillId);
            log.info("一共秒杀出{}件商品", seckillCount);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return Result.ok();
    }

    @ApiOperation(value = "秒杀五(数据库悲观锁)", nickname = "科帮网")
    @PostMapping("/startDPCC_TWO")
    public Result startDPCC_TWO(long seckillId) {
        int skillNum = 1000;
        final CountDownLatch latch = new CountDownLatch(skillNum);
        seckillService.deleteSeckill(seckillId);
        final long killId = seckillId;
        log.info("开始秒杀五(正常、数据库锁最优实现)");
        for (int i = 0; i < 1000; i++) {
            final long userId = i;
            Runnable task = () -> {
                Result result = seckillService.startSeckilDBPCC_TWO(killId, userId);
                log.info("用户:{}{}", userId, result.get("msg"));
                latch.countDown();
            };
            executor.execute(task);
        }
        try {
            latch.await();// 等待所有人任务结束
            Long seckillCount = seckillService.getSeckillCount(seckillId);
            log.info("一共秒杀出{}件商品", seckillCount);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return Result.ok();
    }

    @ApiOperation(value = "秒杀六(数据库乐观锁)", nickname = "科帮网")
    @PostMapping("/startDBOCC")
    public Result startDBOCC(long seckillId) {
        int skillNum = 1000;
        final CountDownLatch latch = new CountDownLatch(skillNum);
        seckillService.deleteSeckill(seckillId);
        final long killId = seckillId;
        log.info("开始秒杀六(正常、数据库锁最优实现)");
        for (int i = 0; i < 1000; i++) {
            final long userId = i;
            Runnable task = () -> {
                //这里使用的乐观锁、可以自定义抢购数量、如果配置的抢购人数比较少、比如120:100(人数:商品) 会出现少买的情况
                //用户同时进入会出现更新失败的情况
                Result result = seckillService.startSeckilDBOCC(killId, userId, 1);
                log.info("用户:{}{}", userId, result.get("msg"));
                latch.countDown();
            };
            executor.execute(task);
        }
        try {
            latch.await();// 等待所有人任务结束
            Long seckillCount = seckillService.getSeckillCount(seckillId);
            log.info("一共秒杀出{}件商品", seckillCount);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return Result.ok();
    }

    @ApiOperation(value = "秒杀柒(进程内队列)", nickname = "科帮网")
    @PostMapping("/startQueue")
    public Result startQueue(long seckillId) {
        seckillService.deleteSeckill(seckillId);
        final long killId = seckillId;
        log.info("开始秒杀柒(正常)");
        for (int i = 0; i < 1000; i++) {
            final long userId = i;
            Runnable task = () -> {
                SuccessKilled kill = new SuccessKilled();
                kill.setSeckillId(killId);
                kill.setUserId(userId);
                Boolean flag = SeckillQueue.getSkillQueue().produce(kill);
                /**
                 * 虽然进入了队列，但是不一定能秒杀成功 进队列出队有间隙
                 */
                if (flag) {
                    //log.info("用户:{}{}",kill.getUserId(),"秒杀成功");
                } else {
                    //log.info("用户:{}{}",userId,"秒杀失败");
                }
            };
            executor.execute(task);
        }
        try {
            Thread.sleep(10000);
            Long seckillCount = seckillService.getSeckillCount(seckillId);
            log.info("一共秒杀出{}件商品", seckillCount);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return Result.ok();
    }

    @ApiOperation(value = "秒杀柒(Disruptor队列)", nickname = "科帮网")
    @PostMapping("/startDisruptorQueue")
    public Result startDisruptorQueue(long seckillId) {
        seckillService.deleteSeckill(seckillId);
        final long killId = seckillId;
        log.info("开始秒杀八(正常)");
        for (int i = 0; i < 1000; i++) {
            final long userId = i;
            Runnable task = () -> {
                SeckillEvent kill = new SeckillEvent();
                kill.setSeckillId(killId);
                kill.setUserId(userId);
                DisruptorUtil.producer(kill);
            };
            executor.execute(task);
        }
        try {
            Thread.sleep(10000);
            Long seckillCount = seckillService.getSeckillCount(seckillId);
            log.info("一共秒杀出{}件商品", seckillCount);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return Result.ok();
    }

}

