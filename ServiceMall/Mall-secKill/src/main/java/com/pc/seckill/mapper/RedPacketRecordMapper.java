package com.pc.seckill.mapper;

import com.pc.seckill.common.entity.RedPacketRecord;
import org.springframework.stereotype.Repository;

@Repository
public interface RedPacketRecordMapper {
    /**
     * 保存抢红包记录
     *
     * @param record record
     */
    void saveResult(RedPacketRecord record);
}
