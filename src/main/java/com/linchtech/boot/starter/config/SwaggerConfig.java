package com.linchtech.boot.starter.config;

import com.fasterxml.classmate.TypeResolver;
import com.linchtech.boot.starter.common.ResultVO;
import com.linchtech.boot.starter.properties.SwaggerProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.http.HttpProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.ApiSelectorBuilder;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author 107
 * @date Created by 2020/4/25 14:58
 */
@Slf4j
@Configuration
@EnableSwagger2
@EnableConfigurationProperties(SwaggerProperties.class)
public class SwaggerConfig {

	/**
	 * swagger 的api 扫描包可以自定义配置，也可以单独的采用 @ApiIgnore 进行忽略
	 *
	 * @return 文档结构
	 */
	@Bean
	public Docket api(SwaggerProperties swaggerProperties, TypeResolver typeResolver) {
		ApiSelectorBuilder apiSelectorBuilder = new Docket(DocumentationType.SWAGGER_2)
				.apiInfo(swaggerProperties.getApiInfo())
				.enable(swaggerProperties.isEnable())
				.globalOperationParameters(swaggerProperties.getHeaders())
				.select()
				.paths(PathSelectors.any());
		if (CollectionUtils.isEmpty(swaggerProperties.getBasePackages())) {
			// TODO 扫描包结构
			apiSelectorBuilder.apis(RequestHandlerSelectors.basePackage("com.linchtech"));
		} else {
			for (String basePackage : swaggerProperties.getBasePackages()) {
				apiSelectorBuilder.apis(RequestHandlerSelectors.basePackage(basePackage));
			}
		}

		return apiSelectorBuilder.build().additionalModels(typeResolver.resolve(ResultVO.class));
	}
}
