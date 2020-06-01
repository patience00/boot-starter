package com.linchtech.boot.starter.common.exceptions;

import com.linchtech.boot.starter.common.HttpResult;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author 107
 * @date 2019/1/14 15:05
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ParameterException extends RuntimeException {

    private Integer code;
    private String message;

    public ParameterException(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public static ParameterException build(String message) {
        return new ParameterException(HttpResult.FAIL.getCode(), message);
    }

}
