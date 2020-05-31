package com.linchtech.boot.starter.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author 107
 * @date Created by 2018/5/11 13:34
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {

	/**
	 * 跨域设置
	 *
	 * @param registry 注册器
	 */
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
				.allowedMethods("POST", "GET", "PUT", "OPTIONS", "DELETE")
				.allowedOrigins("*")
				.allowedHeaders("*")
				.maxAge(3600)
				.allowCredentials(true);
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new WebInterceptor()).addPathPatterns("/**");
	}
}
