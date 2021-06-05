package com.pc.system.controller;

import com.pc.core.model.ResponseBean;
import com.pc.core.utils.ResponseUtil;
import com.pc.core.utils.web.domain.Server;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 服务器监控
 *
 * @author qhl
 */
@RestController
@RequestMapping("/pc/base/monitor")
public class ServerController {
    @RequestMapping(value = "/server", method = RequestMethod.GET)
    public ResponseBean getInfo() throws Exception {
        Server server = new Server();
        server.copyTo();
        return ResponseUtil.success(server);
    }
}
