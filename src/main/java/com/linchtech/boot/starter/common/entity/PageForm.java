package com.linchtech.boot.starter.common.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author: 107
 * @date: 2019-03-17 18:44
 * @description:
 **/
@Data
public class PageForm {

    @ApiModelProperty("当前页索引")
    @NotNull(message = "当前页不能为空")
    @Min(1)
    private Integer pageIndex;

    @ApiModelProperty("当前页大小")
    @NotNull(message = "当前页大小不能为空")
    private Integer pageSize;

    @ApiModelProperty("排序方向:正序ASC/倒序DESC")
    private String orderType;

    @ApiModelProperty("排序字段")
    private String orderField;

}
