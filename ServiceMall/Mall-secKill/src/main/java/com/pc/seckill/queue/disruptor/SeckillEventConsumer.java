package com.pc.seckill.queue.disruptor;

import com.lmax.disruptor.EventHandler;
import com.pc.seckill.common.config.SpringUtil;
import com.pc.seckill.common.entity.Result;
import com.pc.seckill.common.enums.SeckillStatEnum;
import com.pc.seckill.service.ISeckillService;
import lombok.extern.slf4j.Slf4j;

/**
 * 消费者(秒杀处理器)
 * 创建者 科帮网
 */
@Slf4j
public class SeckillEventConsumer implements EventHandler<SeckillEvent> {

    private ISeckillService seckillService = (ISeckillService) SpringUtil.getBean("seckillService");

    @Override
    public void onEvent(SeckillEvent seckillEvent, long seq, boolean bool) {
        Result result =
                seckillService.startSeckilAopLock(seckillEvent.getSeckillId(), seckillEvent.getUserId());
        if (result.equals(Result.ok(SeckillStatEnum.SUCCESS))) {
            log.info("用户:{}{}", seckillEvent.getUserId(), "秒杀成功");
        }
    }
}
