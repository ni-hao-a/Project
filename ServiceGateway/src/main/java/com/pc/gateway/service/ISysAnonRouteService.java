package com.pc.gateway.service;

import com.pc.gateway.model.route.SysAnonRoute;

import java.util.List;

/**
 * 匿名路由配置
 *
 * @author qhl
 */
public interface ISysAnonRouteService {
    String[] getAnonRouteById(String sysId);

    List<SysAnonRoute> getAllAnonRoute();
}
