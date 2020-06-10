package com.linchtech.boot.starter.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author 107
 * @date 2020-06-10 13:35
 * @since 1.0.0
 **/
@Configuration
@ConfigurationProperties(prefix = "system.dingtalk")
@Data
public class DingTalkConfig {

    /**
     * 秘钥
     */
    private String secret;
    /**
     * 群聊发送的token
     */
    private String token;
    /**
     * 需要at的手机号
     */
    private List<String> atMobiles;

}
