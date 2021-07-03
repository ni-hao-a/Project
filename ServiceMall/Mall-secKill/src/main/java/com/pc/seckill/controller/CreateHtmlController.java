package com.pc.seckill.controller;

import com.pc.seckill.common.entity.Result;
import com.pc.seckill.service.ICreateHtmlService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags ="生成静态商品页")
@RestController
@RequestMapping("/createHtml")
@Slf4j
public class CreateHtmlController {

	@Autowired
	private ICreateHtmlService createHtmlService;
	
	@ApiOperation(value="生成静态商品页",nickname="科帮网")
	@PostMapping("/start")
	public Result start(){
		log.info("生成秒杀活动静态页");
		return createHtmlService.createAllHtml();
	}
}
