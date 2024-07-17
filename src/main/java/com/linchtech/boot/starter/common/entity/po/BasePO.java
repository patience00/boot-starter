package com.linchtech.boot.starter.common.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * @author: 107
 * @date: 2019-08-25 21:33
 * @description:
 **/
@Data
@Getter
@Setter
public class BasePO implements Serializable {

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("gmt_create")
    @JsonIgnore
    private Date gmtCreate;

    @TableField("gmt_modified")
    @JsonIgnore
    private Date gmtModified;

    @TableField(value = "delete_flag", select = false)
    @TableLogic
    @JsonIgnore
    private Boolean deleteFlag;
}
