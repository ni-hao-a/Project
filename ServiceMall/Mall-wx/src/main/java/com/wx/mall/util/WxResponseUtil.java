package com.wx.mall.util;

import com.wx.core.util.ResponseUtil;

/**
 * 微信接口枚举信息的响应
 *
 * @author CHENBO
 * @QQ 623659388
 * @since 1.0.0
 */
public class WxResponseUtil extends ResponseUtil {

    /**
     * 按枚举返回错误响应结果
     *
     * @param
     * @return
     */
    public static Object fail(WxResponseCode responseCode) {
        return fail(responseCode.code(), responseCode.desc());
    }
}
