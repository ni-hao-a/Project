package com.pc.service.service;

import com.pc.service.mapper.DtsSystemMapper;
import com.pc.service.domain.DtsSystem;
import com.pc.service.domain.DtsSystemExample;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class DtsSystemConfigService {
	@Resource
	private DtsSystemMapper systemMapper;

	public List<DtsSystem> queryAll() {
		DtsSystemExample example = new DtsSystemExample();
		example.or();
		return systemMapper.selectByExample(example);
	}
}
