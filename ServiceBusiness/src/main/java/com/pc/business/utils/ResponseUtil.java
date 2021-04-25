package com.pc.business.utils;

import com.pc.business.exception.ResponseCodeEnum;
import com.pc.business.exception.ExceptionResponseBean;
import com.pc.business.model.pub.ResponseBean;

public class ResponseUtil {
    public static ExceptionResponseBean setError(ResponseCodeEnum error) {
        ExceptionResponseBean responseBean = new ExceptionResponseBean();
        responseBean.setCode(error.getCode());
        responseBean.setMessage(error.getMessage());
        return responseBean;
    }

    /**
     * 成功结果
     *
     * @param param 实体
     * @return 响应体
     */
    public static ResponseBean success(Object param) {
        ResponseBean resp = new ResponseBean();
        resp.setCode(ResponseCodeEnum.SUCCESS.getCode());
        resp.setMessage(ResponseCodeEnum.SUCCESS.getMessage());
        resp.setData(param);
        return resp;
    }

    /**
     * 失败结果
     *
     * @return 响应体
     */
    public static ResponseBean error(ResponseCodeEnum param) {
        ResponseBean resp = new ResponseBean();
        resp.setCode(param.getCode());
        resp.setMessage(param.getMessage());
        resp.setData(null);
        return resp;
    }
}
