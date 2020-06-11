package com.linchtech.boot.starter.common.dingding;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author 107
 * @date 2019/8/7 14:19
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DingdingMessageTextDTO implements Serializable {

    private String msgtype;
    private MessageAtDTO at;
    private MessageTextDTO text;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MessageTextDTO implements Serializable {
        private String content;
    }

}