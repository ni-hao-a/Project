package com.pc.gateway.service.impl;

import com.pc.gateway.mapper.SysAnonRouteMapper;
import com.pc.gateway.model.route.SysAnonRoute;
import com.pc.gateway.service.ISysAnonRouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 匿名路由配置
 *
 * @author qhl
 */
@Service
public class SysAnonRouteServiceImpl implements ISysAnonRouteService {

    @Autowired
    private SysAnonRouteMapper anonRouteMapper;

    /**
     * 通过id获取匿名路由
     *
     * @param sysId 标识id
     * @return 匿名路由集
     */
    @Override
    public String[] getAnonRouteById(String sysId) {
        List<SysAnonRoute> routes = anonRouteMapper.getAnonRouteById(sysId);
        List<String> routeList = new ArrayList<>();
        for (SysAnonRoute route : routes) {
            routeList.add(route.getSysPath());
        }
        return routeList.toArray(new String[routeList.size()]);
    }

    @Override
    public List<SysAnonRoute> getAllAnonRoute() {
        return anonRouteMapper.getAllAnonRoute();
    }
}
