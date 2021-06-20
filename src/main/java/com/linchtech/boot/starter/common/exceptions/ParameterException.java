package com.linchtech.boot.starter.common.exceptions;

import com.linchtech.boot.starter.common.BaseEnum;
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

    public ParameterException(BaseEnum baseEnum) {
        this.code = baseEnum.code();
        this.message = baseEnum.msg();
    }

}
