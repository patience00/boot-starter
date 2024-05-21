package com.linchtech.boot.starter.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // 匹配所有路径
                        .allowedOrigins("*") // 允许所有来源，生产环境应指定确切的前端域名
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // 允许的HTTP方法
                        .allowedHeaders("*") // 允许所有请求头
                        .allowCredentials(true) // 如果需要携带Cookie，设为true
                        .maxAge(3600); // 预检请求的有效期，单位秒
            }
        };
    }
}