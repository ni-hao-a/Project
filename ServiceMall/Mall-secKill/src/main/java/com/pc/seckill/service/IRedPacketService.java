package com.pc.seckill.service;

import com.pc.seckill.common.entity.RedPacket;
import com.pc.seckill.common.entity.Result;

public interface IRedPacketService {

    /**
     * 获取红包
     *
     * @param redPacketId id
     * @return RedPacket
     */
    RedPacket get(long redPacketId);

    /**
     * 抢红包业务实现
     *
     * @param redPacketId id
     * @return Result
     */
    Result startSeckil(long redPacketId, int userId);

    /**
     * 微信抢红包业务实现
     *
     * @param redPacketId id
     * @param userId
     * @return Result
     */
    Result startTwoSeckil(long redPacketId, int userId);

}
