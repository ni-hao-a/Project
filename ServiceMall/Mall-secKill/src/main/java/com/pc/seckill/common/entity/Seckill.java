package com.pc.seckill.common.entity;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
public class Seckill implements Serializable {
	//private static final long serialVersionUID = 1L;
	private long seckillId;
	private String name;
	private int number;
	private Timestamp startTime;
	private Timestamp endTime;
	private Timestamp createTime;
	private int version;
	private String picUrl;
	private long userId;
}
