package com.pc.seckill.common.entity;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 红包信息表
 */
@Data
public class RedPacket implements Serializable {

    //private static final long serialVersionUID = 1L;
    private long id;
    private long redPacketId;//红包id
    private int totalAmount;//红包总金额，单位分
    private int totalPacket;//红包总个数
    private int type;//类型
    private int uid;//创建用户
    private Timestamp createTime;//创建时间
    private int version;
}
