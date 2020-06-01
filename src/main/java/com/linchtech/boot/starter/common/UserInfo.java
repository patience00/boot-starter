package com.linchtech.boot.starter.common;

import lombok.Builder;
import lombok.Data;

/**
 * @author 107
 * @date  2018/8/8 10:05
 **/
@Data
@Builder
public class UserInfo {

    private Long userId;
    private String location;
    private String ip;

}
