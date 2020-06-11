package com.linchtech.boot.starter.common.dingding;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author 107
 * @date 2019/8/7 14:19
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MessageAtDTO implements Serializable {

    private List<String> atMobiles;
    private Boolean isAtAll;
}