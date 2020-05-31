package com.linchtech.boot.starter.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 107
 * @date 2019/2/20 14:43
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccessUser {

    private Long userId;
    private String location;
    private String ip;

}
