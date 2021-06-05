package com.pc.business.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pc.business.service.ISysDictDataService;
import com.pc.business.service.ISysDictTypeService;
import com.pc.business.utils.feignService.RSysUserFeignService;
import com.pc.core.model.ResponseBean;
import com.pc.core.poi.ExcelUtil;
import com.pc.core.utils.ResponseUtil;
import com.pc.core.utils.StringUtils;
import com.pc.model.rlzy.entity.SysDictData;
import com.pc.model.rlzy.req.DictDataReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * 数据字典信息
 *
 * @author qhl
 */
@RestController
@RequestMapping("/pc/base/business/v1/rlzy/dictData")
public class SysDictDataController {
    @Autowired
    private ISysDictDataService dictDataService;
    @Autowired
    private ISysDictTypeService dictTypeService;
    @Autowired
    private RSysUserFeignService userFeignService;


    /**
     * 查询字典数据列表
     */
    @RequestMapping(value = "/getDictDataList", method = RequestMethod.POST)
    public ResponseBean getDictDataList(@RequestBody SysDictData req) {
        PageHelper.startPage(req.getPageNum(), req.getPageSize());
        List<SysDictData> list = dictDataService.selectDictDataList(req);
        return ResponseUtil.success(new PageInfo<>(list));
    }

    /**
     * 查询字典数据详细
     */
    @RequestMapping(value = "/getDictDataInfo", method = RequestMethod.POST)
    public ResponseBean getDictDataInfo(@RequestBody DictDataReq req) {
        return ResponseUtil.success(dictDataService.selectDictDataById(req.getDictCode()));
    }

    /**
     * 根据字典类型查询字典数据信息
     */
    @RequestMapping(value = "/getDictDataByType", method = RequestMethod.POST)
    public ResponseBean getDictDataByType(@RequestBody DictDataReq req) {
        List<SysDictData> data = dictTypeService.selectDictDataByType(req.getDictType());
        if (StringUtils.isNull(data)) {
            data = new ArrayList<SysDictData>();
        }
        return ResponseUtil.success(data);
    }

    /**
     * 新增字典数据
     */
    @RequestMapping(value = "/addDictData", method = RequestMethod.POST)
    public ResponseBean addDictData(@Valid @RequestBody SysDictData dict) {
        dict.setCreateBy(userFeignService.getUser().getUserName());
        return ResponseUtil.success(dictDataService.insertDictData(dict));
    }

    /**
     * 修改字典数据
     */
    @RequestMapping(value = "/updateDictData", method = RequestMethod.POST)
    public ResponseBean updateDictData(@Valid @RequestBody SysDictData dict) {
        dict.setUpdateBy(userFeignService.getUser().getUserName());
        return ResponseUtil.success(dictDataService.updateDictData(dict));
    }

    /**
     * 根据id删除字典数据
     */
    @RequestMapping(value = "/delDictDataById", method = RequestMethod.POST)
    public ResponseBean delDictDataById(@RequestBody DictDataReq req) {
        return ResponseUtil.success(dictDataService.deleteDictDataById(req.getDictCode()));
    }

    /**
     * 批量删除字典数据
     */
    @RequestMapping(value = "/delDictData", method = RequestMethod.POST)
    public ResponseBean delDictData(@RequestBody DictDataReq req) {
        return ResponseUtil.success(dictDataService.deleteDictDataByIds(req.getDictCodes()));
    }

    /**
     * 导出字典数据
     */
    @RequestMapping(value = "/exportDictData", method = RequestMethod.POST)
    public ResponseBean exportDictData(SysDictData dictData) {
        List<SysDictData> list = dictDataService.selectDictDataList(dictData);
        ExcelUtil<SysDictData> util = new ExcelUtil<SysDictData>(SysDictData.class);
        return util.exportExcel(list, "字典数据");
    }
}
