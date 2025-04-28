package com.linchtech.boot.starter.common;

/**
 * SystemErrorCode
 *
 * @date 2019/11/1
 * @since 1.0.0
 */
public enum SystemErrorCode {
    /**
     * 未知错误
     */
    UNKNOWN_ERROR(10000, "系统忙，请稍后再试"),

    VALIDATE_ERROR(10001, "参数校验失败"),

    UNAUTHORIZED_ERROR(10002, "您没有权限访问"),
    TOKEN_EXPIRED(10401, "token已过期"),
    /**
     * HttpRequestMethodNotSupportedException
     */
    METHOD_NO_SUPPORT(10003, "接口不支持%s方式调用"),
    /**
     * UnsupportedEncodingException
     */
    SERVICE_NOT_AVAILABLE(10010, "服务暂不可用，请稍后再试"),

    /**
     * 系统运行异常
     */
    SERVICE_ERROR(10086, "服务忙，请稍后再试"),
    ;

    private int code;
    private String msg;

    SystemErrorCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
