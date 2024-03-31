package com.linchtech.boot.starter.common.exceptions;

import com.linchtech.boot.starter.common.BaseEnum;
import com.linchtech.boot.starter.common.entity.vo.ResultVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author 107
 * @date 2020-06-04 22:29
 **/
@Data
@EqualsAndHashCode(callSuper = false)
public class BusinessException extends RuntimeException {
    private Integer code;
    private String message;

    public BusinessException(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public BusinessException(BaseEnum baseEnum) {
        this.code = baseEnum.code();
        this.message = baseEnum.msg();
    }

    public BusinessException(String message) {
        this.code = ResultVO.CODE_FAIL;
        this.message = message;
    }
}
