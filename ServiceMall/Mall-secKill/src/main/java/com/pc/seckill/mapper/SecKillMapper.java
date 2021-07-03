package com.pc.seckill.mapper;

import com.pc.seckill.common.entity.Seckill;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SecKillMapper {
    /**
     * 查询所有秒杀商品
     *
     * @return list
     */
    List<Seckill> findAll();

    /**
     * 重置商品库存
     *
     */
    void updateSecKill(@Param("secKillId") long secKillId);

    /**
     * 校验商品库存
     *
     * @return Seckill
     */
    Seckill getSecKillCount(@Param("secKillId") long secKillId);

    /**
     * 扣除商品库存
     *
     * @return int
     */
    int updateSecKillDed(@Param("secKillId") long secKillId);

    /**
     * 悲观锁-FOR UPDATE
     *
     * @return Seckill
     */
    Seckill getSecKillById(@Param("secKillId") long secKillId);

    /**
     * 乐观锁-version
     *
     * @return int
     */
    int updateStock(@Param("secKillId") long secKillId, @Param("version") int version);

    /**
     * 扣除商品库存并且校验库存
     *
     */
    int updateStockAndDed(@Param("secKillId") long secKillId);

}
