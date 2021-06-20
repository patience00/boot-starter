package com.linchtech.boot.starter.common;

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

    @ApiModelProperty("请求状态码")
    private Integer code;

    @ApiModelProperty("请求数据")
    private T data;

    private String message;

    /**
     * 请求成功
     *
     * @return
     */
    public static <T> ResultVO<T> ok() {
        return ResultVO.<T>builder()
                .code(HttpResult.SUCCESS.getCode())
                .message(HttpResult.SUCCESS.getMessage())
                .build();
    }

    public static <T> ResultVO<T> ok(T data) {
        return ok(HttpResult.SUCCESS, data);
    }

    public static <T> ResultVO<T> ok(BaseEnum baseEnum,
                                     T data) {
        return ResultVO.<T>builder()
                .code(baseEnum.code())
                .message(baseEnum.msg())
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
                .code(HttpResult.FAIL.getCode())
                .message(HttpResult.FAIL.getMessage())
                .build();
    }

    public static <T> ResultVO<T> fail(BaseEnum baseEnum) {
        return fail(baseEnum, baseEnum.msg());
    }

    public static <T> ResultVO<T> fail(T data) {
        return fail(HttpResult.FAIL, data);
    }

    public static <T> ResultVO<T> fail(BaseEnum baseEnum,
                                       T data) {
        return ResultVO.<T>builder()
                .code(baseEnum.code())
                .message(baseEnum.msg())
                .data(data)
                .build();
    }

    public static <T> ResultVO<T> fail(BaseEnum baseEnum,
                                       String message) {
        return ResultVO.<T>builder()
                .code(baseEnum.code())
                .message(message)
                .build();
    }

    public static <T> ResultVO<T> fail(String message) {
        return ResultVO.<T>builder()
                .code(BaseEnum.MSG_FAIL)
                .message(message)
                .data(null)
                .build();
    }

    public static <T> ResultVO<T> fail(Integer code, String message) {
        return ResultVO.<T>builder()
                .code(code)
                .message(message)
                .data(null)
                .build();
    }
}
