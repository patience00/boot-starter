package com.linchtech.boot.starter.common;

/**
 * @author 107
 * @date 2021-06-20 12:34
 **/
public interface BaseEnum {

    String SUCCESS_MSG = "请求成功";
    Integer MSG_OK = 0;
    Integer MSG_FAIL = -1;

    /**
     * code
     *
     * @return code
     */
    int code();

    /**
     * msg
     *
     * @return msg
     */
    String msg();
}
