package com.pc.service.service;

import com.pc.service.mapper.DtsPermissionMapper;
import com.pc.service.domain.DtsPermission;
import com.pc.service.domain.DtsPermissionExample;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class DtsPermissionService {
    @Resource
    private DtsPermissionMapper permissionMapper;

    public Set<String> queryByRoleIds(Integer[] roleIds) {
        Set<String> permissions = new HashSet<String>();
        if (roleIds.length == 0) {
            return permissions;
        }

        DtsPermissionExample example = new DtsPermissionExample();
        example.or().andRoleIdIn(Arrays.asList(roleIds)).andDeletedEqualTo(false);
        List<DtsPermission> permissionList = permissionMapper.selectByExample(example);

        for (DtsPermission permission : permissionList) {
            permissions.add(permission.getPermission());
        }

        return permissions;
    }

    public Set<String> queryByRoleId(Integer roleId) {
        Set<String> permissions = new HashSet<String>();
        if (roleId == null) {
            return permissions;
        }

        DtsPermissionExample example = new DtsPermissionExample();
        example.or().andRoleIdEqualTo(roleId).andDeletedEqualTo(false);
        List<DtsPermission> permissionList = permissionMapper.selectByExample(example);

        for (DtsPermission permission : permissionList) {
            permissions.add(permission.getPermission());
        }

        return permissions;
    }

    public boolean checkSuperPermission(Integer roleId) {
        if (roleId == null) {
            return false;
        }

        DtsPermissionExample example = new DtsPermissionExample();
        example.or().andRoleIdEqualTo(roleId).andPermissionEqualTo("*").andDeletedEqualTo(false);
        return permissionMapper.countByExample(example) != 0;
    }

    public void deleteByRoleId(Integer roleId) {
        DtsPermissionExample example = new DtsPermissionExample();
        example.or().andRoleIdEqualTo(roleId).andDeletedEqualTo(false);
        permissionMapper.logicalDeleteByExample(example);
    }

    public void add(DtsPermission DtsPermission) {
        DtsPermission.setAddTime(LocalDateTime.now());
        DtsPermission.setUpdateTime(LocalDateTime.now());
        permissionMapper.insertSelective(DtsPermission);
    }
}
