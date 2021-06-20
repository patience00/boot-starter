package com.linchtech.boot.starter.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;
import java.util.Map;

/**
 * @author 107
 * @date Created by 2020/4/25 14:59
 */
@Data
@ConfigurationProperties(prefix = "swagger")
public class SwaggerProperties {

    private boolean enable = false;

    private List<String> basePackages;
    /**
     * 自定义header
     */
    private Map<String, String> headers;

    private String name;
    private String email;
    private String url;
    /**
     * 文档标题
     */
    private String title = "在线文档";
    /**
     * 文档描述
     */
    private String description = "仅用于开发和联调测试阶段使用";
    /**
     * 文档版本号
     */
    private String version = "1.0.0";
    private String license = "";
    private String licenseUrl = "";

}
