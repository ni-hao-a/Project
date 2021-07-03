package com.pc.seckill.queue.jvm;

import com.pc.seckill.common.entity.Result;
import com.pc.seckill.common.entity.SuccessKilled;
import com.pc.seckill.common.enums.SeckillStatEnum;
import com.pc.seckill.service.ISeckillService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * 消费秒杀队列
 * 创建者 科帮网
 * 创建时间	2018年4月3日
 */
@Component
@Slf4j
public class TaskRunner implements ApplicationRunner {

    @Autowired
    private ISeckillService seckillService;

    @Override
    public void run(ApplicationArguments var) {
        new Thread(() -> {
            log.info("提醒队列启动成功");
            while (true) {
                try {
                    //进程内队列
                    SuccessKilled kill = SeckillQueue.getSkillQueue().consume();
                    if (kill != null) {
                        Result result =
                                seckillService.startSeckilAopLock(kill.getSeckillId(), kill.getUserId());
                        if (result.equals(Result.ok(SeckillStatEnum.SUCCESS))) {
                            log.info("用户:{}{}", kill.getUserId(), "秒杀成功");
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}