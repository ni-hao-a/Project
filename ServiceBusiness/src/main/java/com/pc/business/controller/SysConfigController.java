package com.pc.business.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pc.business.service.ISysConfigService;
import com.pc.business.utils.feignService.RSysUserFeignService;
import com.pc.core.constants.UserConstants;
import com.pc.core.exception.ResponseCodeEnum;
import com.pc.core.model.ResponseBean;
import com.pc.core.poi.ExcelUtil;
import com.pc.core.utils.ResponseUtil;
import com.pc.model.rlzy.entity.SysConfig;
import com.pc.model.rlzy.req.ConfigReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 参数配置 信息操作处理
 *
 * @author qhl
 */
@RestController
@RequestMapping("/pc/base/business/v1/rlzy/config")
public class SysConfigController {
    @Autowired
    private ISysConfigService configService;
    @Autowired
    private RSysUserFeignService userFeignService;

    /**
     * 获取参数配置列表
     */
    @RequestMapping(value = "/getList", method = RequestMethod.POST)
    public ResponseBean getList(@RequestBody SysConfig req) {
        PageHelper.startPage(req.getPageNum(), req.getPageSize());
        List<SysConfig> list = configService.selectConfigList(req);
        return ResponseUtil.success(new PageInfo<>(list));
    }

    /**
     * 获取用户初始化密码配置
     */
    @RequestMapping(value = "/getConfig", method = RequestMethod.POST)
    public ResponseBean getConfig(@RequestBody ConfigReq req) {
        return ResponseUtil.success(configService.selectConfigByKey(req.getConfigKey()));
    }

    /**
     * 根据参数编号获取详细信息
     */
    @RequestMapping(value = "/getConfigInfo", method = RequestMethod.POST)
    public ResponseBean getConfigInfo(@RequestBody ConfigReq req) {
        return ResponseUtil.success(configService.selectConfigById(req.getConfigId()));
    }

    /**
     * 新增参数配置
     */
    @RequestMapping(value = "/addConfig", method = RequestMethod.POST)
    public ResponseBean addConfig(@Validated @RequestBody SysConfig config) {
        if (UserConstants.NOT_UNIQUE.equals(configService.checkConfigKeyUnique(config))) {
            return ResponseUtil.error(ResponseCodeEnum.ADD_CONFIG_FAILURE);
        }
        config.setCreateBy(userFeignService.getUser().getUserName());
        return ResponseUtil.success(configService.insertConfig(config));
    }

    /**
     * 修改参数配置
     */
    @RequestMapping(value = "/updateConfig", method = RequestMethod.POST)
    public ResponseBean updateConfig(@Validated @RequestBody SysConfig config) {
        if (UserConstants.NOT_UNIQUE.equals(configService.checkConfigKeyUnique(config))) {
            return ResponseUtil.error(ResponseCodeEnum.UPDATE_CONFIG_FAILURE);
        }
        config.setUpdateBy(userFeignService.getUser().getUserName());
        return ResponseUtil.success(configService.updateConfig(config));
    }

    /**
     * 批量删除参数配置
     */
    @RequestMapping(value = "/delConfigList", method = RequestMethod.POST)
    public ResponseBean delConfigList(@RequestBody ConfigReq req) {
        return ResponseUtil.success(configService.deleteConfigByIds(req.getIds()));
    }

    /**
     * 清空缓存配置
     */
    @RequestMapping(value = "/clearCacheConfig", method = RequestMethod.POST)
    public ResponseBean clearCacheConfig() {
        configService.clearCache();
        return ResponseUtil.success("清理成功");
    }

    /**
     * 导出配置参数
     *
     * @param config 配置参数
     * @return excel
     */
    @RequestMapping(value = "/exportConfig", method = RequestMethod.POST)
    public ResponseBean export(@RequestBody SysConfig config) {
        List<SysConfig> list = configService.selectConfigList(config);
        ExcelUtil<SysConfig> util = new ExcelUtil<SysConfig>(SysConfig.class);
        return ResponseUtil.success(util.exportExcel(list, "参数数据"));
    }
}
