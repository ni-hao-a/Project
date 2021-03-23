package com.pc.business.utils;

import com.pc.business.exception.ErrorCodeEnum;
import com.pc.business.model.ResponseBean;

public class ResponseUtil {
    public static ResponseBean setError(ErrorCodeEnum error) {
        ResponseBean responseBean = new ResponseBean();
        responseBean.setCode(error.getCode());
        responseBean.setMessage(error.getMessage());
        return responseBean;
    }
}
