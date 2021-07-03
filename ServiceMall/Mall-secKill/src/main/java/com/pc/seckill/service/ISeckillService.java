package com.pc.seckill.service;

import com.pc.seckill.common.entity.Result;
import com.pc.seckill.common.entity.Seckill;

import java.util.List;

public interface ISeckillService {

    /**
     * 查询全部的秒杀记录
     *
     * @return List<Seckill>
     */
    List<Seckill> getSeckillList();

    /**
     * 查询单个秒杀记录
     *
     * @param seckillId id
     * @return Seckill
     */
    Seckill getById(long seckillId);

    /**
     * 查询秒杀售卖商品
     *
     * @param seckillId id
     * @return Long
     */
    Long getSeckillCount(long seckillId);

    /**
     * 删除秒杀售卖商品记录
     *
     * @param seckillId id
     */
    void deleteSeckill(long seckillId);

    /**
     * 秒杀 一、会出现数量错误
     *
     * @param seckillId seckillId
     * @param userId    userId
     * @return Result
     */
    Result startSeckil(long seckillId, long userId);

    /**
     * 秒杀 二、程序锁
     *
     * @param seckillId seckillId
     * @param userId    userId
     * @return Result
     */
    Result startSeckilLock(long seckillId, long userId);

    /**
     * 秒杀 二、程序锁AOP
     *
     * @param seckillId seckillId
     * @param userId    userId
     * @return Result
     */
    Result startSeckilAopLock(long seckillId, long userId);

    /**
     * 秒杀 二、数据库悲观锁
     *
     * @param seckillId seckillId
     * @param userId    userId
     * @return Result
     */
    Result startSeckilDBPCC_ONE(long seckillId, long userId);

    /**
     * 秒杀 三、数据库悲观锁
     *
     * @param seckillId seckillId
     * @param userId    userId
     * @return Result
     */
    Result startSeckilDBPCC_TWO(long seckillId, long userId);

    /**
     * 秒杀 三、数据库乐观锁
     *
     * @param seckillId seckillId
     * @param userId    userId
     * @return Result
     */
    Result startSeckilDBOCC(long seckillId, long userId, long number);

    /**
     * 秒杀 四、事物模板
     *
     * @param seckillId seckillId
     * @param userId    userId
     * @return Result
     */
    Result startSeckilTemplate(long seckillId, long userId, long number);

}
