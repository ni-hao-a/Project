package com.pc.seckill.common.entity;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
public class SuccessKilled implements Serializable{
	//private static final long serialVersionUID = 1L;
	private long userId;
	private long seckillId;
	private short state;
	private Timestamp createTime;
}
