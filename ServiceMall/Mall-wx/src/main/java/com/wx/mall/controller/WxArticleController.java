package com.wx.mall.controller;

import com.pc.service.domain.DtsArticle;
import com.pc.service.service.DtsArticleService;
import com.wx.core.util.ResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;

/**
 * 用户收货地址服务
 */
@RestController
@RequestMapping("/wx/article")
@Validated
@Slf4j
public class WxArticleController {

	@Autowired
	private DtsArticleService articleService;

	/**
	 * 文章公告信息
	 *
	 * @param id
	 *            文章ID
	 * @return 文章详情
	 */
	@GetMapping("detail")
	public Object detail(@NotNull Integer id) {
		log.info("【请求开始】获取公告文章,请求参数,id:{}", id);
		DtsArticle article = null;
		try {
			article = articleService.findById(id);
		} catch (Exception e) {
			log.error("获取文章公告失败,文章id：{}", id);
			e.printStackTrace();
		}
		// 这里不打印响应结果，文章内容信息较多
		// log.info("【请求结束】获取公告文章,响应结果：{}",JSONObject.toJSONString(article));
		return ResponseUtil.ok(article);
	}
}
