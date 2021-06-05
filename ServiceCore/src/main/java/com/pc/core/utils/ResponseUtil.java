package com.pc.core.utils;


import com.pc.core.exception.ExceptionResponseBean;
import com.pc.core.exception.ResponseCodeEnum;
import com.pc.core.model.ResponseBean;

public class ResponseUtil {
    public static ExceptionResponseBean setError(ResponseCodeEnum error) {
        ExceptionResponseBean responseBean = new ExceptionResponseBean();
        responseBean.setCode(error.getCode());
        responseBean.setMessage(error.getMessage());
        return responseBean;
    }

    /**
     * 成功结果,无参
     *
     * @return 响应体
     */
    public static ResponseBean success() {
        ResponseBean resp = new ResponseBean();
        resp.setCode(ResponseCodeEnum.SUCCESS.getCode());
        resp.setMsg(ResponseCodeEnum.SUCCESS.getMessage());
        return resp;
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
        resp.setMsg(ResponseCodeEnum.SUCCESS.getMessage());
        resp.setData(param);
        return resp;
    }

    /**
     * 成功结果,自定义响应信息
     *
     * @param param 实体
     * @param msg 响应信息
     * @return 响应体
     */
    public static ResponseBean success(Object param, String msg) {
        ResponseBean resp = new ResponseBean();
        resp.setCode(0);
        resp.setMsg(msg);
        resp.setData(param);
        return resp;
    }

    /**
     * 成功结果,自定义响应码
     *
     * @param param 实体
     * @param code 响应码
     * @return 响应体
     */
    public static ResponseBean success(Object param, int code) {
        ResponseBean resp = new ResponseBean();
        resp.setCode(code);
        resp.setMsg("成功结果");
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
        resp.setMsg(param.getMessage());
        resp.setData(null);
        return resp;
    }

    /**
     * 失败结果--自定义
     *
     * @return 响应体
     */
    public static ResponseBean error(int code, String msg) {
        ResponseBean resp = new ResponseBean();
        resp.setCode(code);
        resp.setMsg(msg);
        resp.setData(null);
        return resp;
    }

    /**
     * 失败结果--自定义
     *
     * @return 响应体
     */
    public static ResponseBean error(String msg) {
        ResponseBean resp = new ResponseBean();
        resp.setCode(1);
        resp.setMsg(msg);
        resp.setData(null);
        return resp;
    }
}
