package com.linchtech.boot.starter.common.dingding;

import lombok.*;

/**
 * @author 107
 * @date 2019/8/7 14:19
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DingdingMessageTextDTO{

    private String msgtype;
    private MessageAtDTO at;
    private MessageTextDTO text;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MessageTextDTO {
        private String content;
    }

}