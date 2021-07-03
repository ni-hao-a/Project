package com.pc.seckill.mapper;

import com.pc.seckill.common.entity.SuccessKilled;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SuccessSecKillMapper {
    /**
     * 统计商品秒杀结果
     *
     * @param secKillId id
     * @return int
     */
    int getSecKillCount(@Param("secKillId") long secKillId);

    /**
     * 清理该商品秒杀结果
     *
     * @param secKillId id
     */
    void delResult(@Param("secKillId") long secKillId);

    /**
     * 保存商品秒杀结果
     *
     * @param successKilled id
     */
    void saveResult(SuccessKilled successKilled);
}
