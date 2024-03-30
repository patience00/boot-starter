package com.linchtech.boot.starter.common.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PageResultVO<T> {

    @ApiModelProperty("起始页")
    private Integer pageIndex;

    @ApiModelProperty("一共多少页")
    private Integer pageNumber;

    @ApiModelProperty("每页多少条")
    private Integer pageSize;

    @ApiModelProperty("总记录数")
    private Long count;

    @ApiModelProperty("数据数组")
    private List<T> data;
}
