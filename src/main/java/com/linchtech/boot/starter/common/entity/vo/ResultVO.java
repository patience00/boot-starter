package com.linchtech.boot.starter.common.entity.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.linchtech.boot.starter.common.BaseEnum;
import com.linchtech.boot.starter.common.SystemErrorCode;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author 107
 * @create 2018-07-23 10:17
 * @desc
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResultVO<T> implements Serializable {

    private static final String SUCCESS_MSG = "请求成功";
    private static final Integer CODE_OK = 0;
    public static final Integer CODE_FAIL = -1;

    // 响应码
    @JsonProperty("code")
    private Integer code = CODE_OK;

    @ApiModelProperty("请求数据")
    private T data;

    @JsonProperty("msg")
    private String msg = SUCCESS_MSG;


    /**
     * 请求成功
     *
     * @return
     */
    public static <T> ResultVO<T> ok() {
        return ResultVO.<T>builder()
                .code(CODE_OK)
                .msg(SUCCESS_MSG)
                .build();
    }

    public static <T> ResultVO<T> ok(T data) {
        return ResultVO.<T>builder()
                .code(CODE_OK)
                .data(data)
                .build();
    }

    /**
     * 请求失败
     *
     * @return
     */
    public static <T> ResultVO<T> fail() {
        return ResultVO.<T>builder()
                .code(CODE_FAIL)
                .build();
    }

    /**
     * 请求失败
     *
     * @return
     */
    public static <T> ResultVO<T> fail(SystemErrorCode systemErrorCode, String msg) {
        return ResultVO.<T>builder()
                .msg(msg)
                .code(systemErrorCode.getCode())
                .build();
    }

    public static <T> ResultVO<T> fail(BaseEnum baseEnum) {
        return fail(baseEnum, null);
    }


    public static <T> ResultVO<T> fail(BaseEnum baseEnum,
                                       T data) {
        return ResultVO.<T>builder()
                .code(baseEnum.code())
                .msg(baseEnum.msg())
                .data(data)
                .build();
    }


    public static <T> ResultVO<T> fail(String message) {
        return ResultVO.<T>builder()
                .msg(message)
                .code(CODE_FAIL)
                .data(null)
                .build();
    }

    public static <T> ResultVO<T> fail(Integer code, String message) {
        return ResultVO.<T>builder()
                .code(code)
                .msg(message)
                .data(null)
                .build();
    }
}
