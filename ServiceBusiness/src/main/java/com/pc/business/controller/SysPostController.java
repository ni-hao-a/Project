package com.pc.business.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pc.business.service.ISysPostService;
import com.pc.business.utils.feignService.RSysUserFeignService;
import com.pc.core.constants.UserConstants;
import com.pc.core.model.ResponseBean;
import com.pc.core.poi.ExcelUtil;
import com.pc.core.utils.ResponseUtil;
import com.pc.model.rlzy.entity.SysPost;
import com.pc.model.rlzy.req.PostReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * 岗位信息操作处理
 *
 * @author qhl
 */
@RestController
@RequestMapping("/pc/base/business/v1/rlzy/post")
public class SysPostController {
    @Autowired
    private ISysPostService postService;
    @Autowired
    private RSysUserFeignService userFeignService;

    /**
     * 获取岗位列表
     */
    @RequestMapping(value = "/getPostList", method = RequestMethod.POST)
    public ResponseBean getPostList(@RequestBody SysPost req) {
        PageHelper.startPage(req.getPageNum(), req.getPageSize());
        List<SysPost> list = postService.selectPostList(req);
        return ResponseUtil.success(new PageInfo<>(list));
    }

    /**
     * 根据岗位编号获取详细信息
     */
    @RequestMapping(value = "/getPostInfoById", method = RequestMethod.POST)
    public ResponseBean getPostInfoById(@RequestBody Long postId) {
        return ResponseUtil.success(postService.selectPostById(postId));
    }

    /**
     * 新增岗位
     */
    @RequestMapping(value = "/addPost", method = RequestMethod.POST)
    public ResponseBean addPost(@Valid @RequestBody SysPost req) {
        if (UserConstants.NOT_UNIQUE.equals(postService.checkPostNameUnique(req))) {
            return ResponseUtil.error(3001, "新增岗位'" + req.getPostName() + "'失败，岗位名称已存在");
        } else if (UserConstants.NOT_UNIQUE.equals(postService.checkPostCodeUnique(req))) {
            return ResponseUtil.error(3002, "新增岗位'" + req.getPostName() + "'失败，岗位编码已存在");
        }
        req.setCreateBy(userFeignService.getUser().getUserName());
        return ResponseUtil.success(postService.insertPost(req));
    }

    /**
     * 修改岗位
     */
    @RequestMapping(value = "/updatePost", method = RequestMethod.POST)
    public ResponseBean updatePost(@Valid @RequestBody SysPost req) {
        if (UserConstants.NOT_UNIQUE.equals(postService.checkPostNameUnique(req))) {
            return ResponseUtil.error(3003, "修改岗位'" + req.getPostName() + "'失败，岗位名称已存在");
        } else if (UserConstants.NOT_UNIQUE.equals(postService.checkPostCodeUnique(req))) {
            return ResponseUtil.error(3004, "修改岗位'" + req.getPostName() + "'失败，岗位编码已存在");
        }
        req.setUpdateBy(userFeignService.getUser().getUserName());
        return ResponseUtil.success(postService.updatePost(req));
    }

    /**
     * 删除岗位
     */
    @RequestMapping(value = "/delPost", method = RequestMethod.POST)
    public ResponseBean delPost(@Valid @RequestBody PostReq req) {
        return ResponseUtil.success(postService.deletePostByIds(req.getPostIds()));
    }

    /**
     * 获取岗位选择框列表
     */
    @RequestMapping(value = "/getAllPost", method = RequestMethod.POST)
    public ResponseBean getAllPost() {
        List<SysPost> posts = postService.selectPostAll();
        return ResponseUtil.success(posts);
    }

    /**
     * 导出岗位表格
     */
    @RequestMapping(value = "/exportPost", method = RequestMethod.POST)
    public ResponseBean exportPost(@RequestBody SysPost req) {
        List<SysPost> list = postService.selectPostList(req);
        ExcelUtil<SysPost> util = new ExcelUtil<SysPost>(SysPost.class);
        return util.exportExcel(list, "岗位数据");
    }
}
