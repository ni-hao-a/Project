package com.pc.business.controller;

import com.pc.business.service.ISysDeptService;
import com.pc.business.utils.feignService.RSysUserFeignService;
import com.pc.core.constants.UserConstants;
import com.pc.core.exception.ResponseCodeEnum;
import com.pc.core.model.ResponseBean;
import com.pc.core.utils.ResponseUtil;
import com.pc.core.utils.StringUtils;
import com.pc.model.rlzy.entity.SysDept;
import com.pc.model.rlzy.req.DeptReq;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 部门信息
 *
 * @author qhl
 */
@RestController
@RequestMapping("/pc/base/business/v1/rlzy/dept")
public class SysDeptController {
    @Autowired
    private ISysDeptService deptService;
    @Autowired
    private RSysUserFeignService userFeignService;

    /**
     * 获取部门列表
     */
    @RequestMapping(value = "/getList", method = RequestMethod.POST)
    public ResponseBean getList(@RequestBody SysDept dept) {
        List<SysDept> deptList = deptService.selectDeptList(dept);
        return ResponseUtil.success(deptList);
    }

    /**
     * 查询部门列表（去除节点）
     */
    @RequestMapping(value = "/getDeptList", method = RequestMethod.POST)
    public ResponseBean getDeptList(@RequestBody DeptReq req) {
        List<SysDept> depts = deptService.selectDeptList(new SysDept());
        Iterator<SysDept> it = depts.iterator();
        while (it.hasNext()) {
            SysDept d = it.next();
            if (d.getDeptId().intValue() == req.getDeptId()
                    || ArrayUtils.contains(StringUtils.split(d.getAncestors(), ","), req.getDeptId() + "")) {
                it.remove();
            }
        }
        return ResponseUtil.success(depts);
    }

    /**
     * 根据部门编号获取详细信息
     */
    @RequestMapping(value = "/getDeptInfoById", method = RequestMethod.POST)
    public ResponseBean getDeptInfoById(@RequestBody DeptReq req) {
        return ResponseUtil.success(deptService.selectDeptById(req.getDeptId()));
    }

    /**
     * 获取部门下拉树列表
     */
    @RequestMapping(value = "/getDeptTree", method = RequestMethod.POST)
    public ResponseBean getDeptTree(@RequestBody SysDept req) {
        List<SysDept> deptList = deptService.selectDeptList(req);
        return ResponseUtil.success(deptService.buildDeptTreeSelect(deptList));
    }

    /**
     * 加载对应角色部门列表树
     */
    @RequestMapping(value = "/getDeptTreeByRole", method = RequestMethod.POST)
    public ResponseBean getDeptTreeByRole(@RequestBody DeptReq req) {
        List<SysDept> deptList = deptService.selectDeptList(new SysDept());
        Map<String, Object> map = new HashMap<>();
        map.put("checkedKeys", deptService.selectDeptListByRoleId(req.getRoleId()));
        map.put("depts", deptService.buildDeptTreeSelect(deptList));
        return ResponseUtil.success(map);
    }

    /**
     * 新增部门
     */
    @RequestMapping(value = "/addDept", method = RequestMethod.POST)
    public ResponseBean add(@Valid @RequestBody SysDept dept) {
        if (UserConstants.NOT_UNIQUE.equals(deptService.checkDeptNameUnique(dept))) {
            return ResponseUtil.error(ResponseCodeEnum.ADD_DEPT_FAILURE);
        }
        dept.setCreateBy(userFeignService.getUser().getUserName());
        return ResponseUtil.success(deptService.insertDept(dept));
    }

    /**
     * 修改部门
     */
    @RequestMapping(value = "/updateDept", method = RequestMethod.POST)
    public ResponseBean updateDept(@Valid @RequestBody SysDept dept) {
        if (UserConstants.NOT_UNIQUE.equals(deptService.checkDeptNameUnique(dept))) {
            return ResponseUtil.error(1001, "修改部门'" + dept.getDeptName() + "'失败，部门名称已存在");
        } else if (dept.getParentId().equals(dept.getDeptId())) {
            return ResponseUtil.error(1002, "修改部门'" + dept.getDeptName() + "'失败，上级部门不能是自己");
        } else if (StringUtils.equals(UserConstants.DEPT_DISABLE, dept.getStatus())
                && deptService.selectNormalChildrenDeptById(dept.getDeptId()) > 0) {
            return ResponseUtil.error(1003, "该部门包含未停用的子部门！");
        }
        dept.setUpdateBy(userFeignService.getUser().getUserName());
        return ResponseUtil.success(deptService.updateDept(dept));
    }

    /**
     * 删除部门
     */
    @RequestMapping(value = "/delDept", method = RequestMethod.POST)
    public ResponseBean delDept(@RequestBody DeptReq req) {
        if (deptService.hasChildByDeptId(req.getDeptId())) {
            return ResponseUtil.error(1004, "存在下级部门,不允许删除");
        }
        if (deptService.checkDeptExistUser(req.getDeptId())) {
            return ResponseUtil.error(1005, "部门存在用户,不允许删除");
        }
        return ResponseUtil.success(deptService.deleteDeptById(req.getDeptId()));
    }
}
