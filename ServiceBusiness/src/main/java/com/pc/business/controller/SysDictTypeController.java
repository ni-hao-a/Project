package com.pc.business.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pc.business.service.ISysDictTypeService;
import com.pc.business.utils.feignService.RSysUserFeignService;
import com.pc.core.model.ResponseBean;
import com.pc.core.poi.ExcelUtil;
import com.pc.core.utils.ResponseUtil;
import com.pc.model.rlzy.contants.UserConstants;
import com.pc.model.rlzy.entity.SysDictType;
import com.pc.model.rlzy.req.DictTypeReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * 数据字典信息
 *
 * @author qhl
 */
@RestController
@RequestMapping("/pc/base/business/v1/rlzy/dictType")
public class SysDictTypeController {
    @Autowired
    private ISysDictTypeService dictTypeService;
    @Autowired
    private RSysUserFeignService userFeignService;

    /**
     * 查询字典类型列表
     */
    @RequestMapping(value = "/getDictTypeList", method = RequestMethod.POST)
    public ResponseBean getDictTypeList(@RequestBody SysDictType req) {
        PageHelper.startPage(req.getPageNum(), req.getPageSize());
        List<SysDictType> list = dictTypeService.selectDictTypeList(req);
        return ResponseUtil.success(new PageInfo<>(list));
    }

    /**
     * 查询字典类型详细
     */
    @RequestMapping(value = "/getDictTypeInfo", method = RequestMethod.POST)
    public ResponseBean getDictTypeInfo(@RequestBody DictTypeReq req) {
        return ResponseUtil.success(dictTypeService.selectDictTypeById(req.getDictId()));
    }

    /**
     * 新增字典类型
     */
    @RequestMapping(value = "/addDictType", method = RequestMethod.POST)
    public ResponseBean addDictType(@Valid @RequestBody SysDictType dict) {
        if (UserConstants.NOT_UNIQUE.equals(dictTypeService.checkDictTypeUnique(dict))) {
            return ResponseUtil.error(5001,"新增字典'" + dict.getDictName() + "'失败，字典类型已存在");
        }
        dict.setCreateBy(userFeignService.getUser().getUserName());
        return ResponseUtil.success(dictTypeService.insertDictType(dict));
    }

    /**
     * 修改字典类型
     */
    @RequestMapping(value = "/updateDictType", method = RequestMethod.POST)
    public ResponseBean updateDictType(@Valid @RequestBody SysDictType dict) {
        if (UserConstants.NOT_UNIQUE.equals(dictTypeService.checkDictTypeUnique(dict))) {
            return ResponseUtil.error(5002,"修改字典'" + dict.getDictName() + "'失败，字典类型已存在");
        }
        dict.setUpdateBy(userFeignService.getUser().getUserName());
        return ResponseUtil.success(dictTypeService.updateDictType(dict));
    }

    /**
     * 删除字典类型
     */
    @RequestMapping(value = "/delDictType", method = RequestMethod.POST)
    public ResponseBean delDictType(@RequestBody DictTypeReq req) {
        return ResponseUtil.success(dictTypeService.deleteDictTypeByIds(req.getDictIds()));
    }

    /**
     * 清空缓存
     */
    @RequestMapping(value = "/clearCache", method = RequestMethod.GET)
    public ResponseBean clearCache() {
        dictTypeService.clearCache();
        return ResponseUtil.success("清理成功");
    }

    /**
     * 获取字典选择框列表
     */
    @RequestMapping(value = "/getOptionSelect", method = RequestMethod.GET)
    public ResponseBean getOptionSelect() {
        List<SysDictType> dictTypes = dictTypeService.selectDictTypeAll();
        return ResponseUtil.success(dictTypes);
    }

    /**
     * 导出字典
     */
    @RequestMapping(value = "/exportDictType", method = RequestMethod.POST)
    public ResponseBean export(@RequestBody SysDictType dictType) {
        List<SysDictType> list = dictTypeService.selectDictTypeList(dictType);
        ExcelUtil<SysDictType> util = new ExcelUtil<SysDictType>(SysDictType.class);
        return util.exportExcel(list, "字典类型");
    }
}
