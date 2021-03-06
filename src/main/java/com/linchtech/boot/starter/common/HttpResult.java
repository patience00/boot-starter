package com.linchtech.boot.starter.common;

import lombok.Getter;

/**
 * @author 107
 * @date 2018-07-23 10:24
 **/
@Getter
public enum HttpResult implements BaseEnum{

    /**
     * 请求成功返回码.
     */
    SUCCESS("请求成功", 0),

    /**
     * 请求失败返回描述及返回码.
     */
    FAIL("请求失败", -1),

    /**
     * 不合法的参数返回描述及返回码.
     */
    PARAMETER_ERROR("不合法的参数", 1),
    BUSINESS_ERROR("业务异常", 2),
    METHOD_ERROR("不支持的请求方式", 3),

    /**
     * 系统异常返回描述及返回码.
     */
    SYSTEM_ERROR("系统错误", 3);

    private Integer code;

    private String message;

    HttpResult(String message, Integer code) {
        this.code = code;
        this.message = message;
    }

    /**
     * code
     *
     * @return code
     */
    @Override
    public int code() {
        return code;
    }

    /**
     * msg
     *
     * @return msg
     */
    @Override
    public String msg() {
        return message;
    }
}