package com.linchtech.boot.starter.config;

import com.fasterxml.classmate.TypeResolver;
import com.linchtech.boot.starter.common.ResultVO;
import com.linchtech.boot.starter.properties.SwaggerProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.ApiSelectorBuilder;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    @ConditionalOnMissingBean(Docket.class)
    public Docket api(SwaggerProperties swaggerProperties, TypeResolver typeResolver) {
        List<Parameter> headers = new ArrayList<>();
        Map<String, String> headerKeys = swaggerProperties.getHeaders();
        if (!CollectionUtils.isEmpty(headerKeys)) {
            // 添加自定义header
            for (String key : headerKeys.keySet()) {
                ParameterBuilder parameterBuilder = new ParameterBuilder();
                parameterBuilder.allowEmptyValue(false);
                parameterBuilder.defaultValue(headerKeys.get(key));
                parameterBuilder.name(key);
                parameterBuilder.required(false);
                parameterBuilder.description(key);
                parameterBuilder.hidden(false);
                parameterBuilder.allowMultiple(false);
                parameterBuilder.modelRef(new ModelRef("string"));
                parameterBuilder.parameterType("header");
                headers.add(parameterBuilder.build());
            }
        }
        ApiInfo apiInfo = new ApiInfoBuilder().title(swaggerProperties.getTitle())
                .description(swaggerProperties.getDescription())
                .license(swaggerProperties.getLicense())
                .licenseUrl(swaggerProperties.getLicenseUrl())
                .build();

        ApiSelectorBuilder apiSelectorBuilder = new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo)
                .enable(swaggerProperties.isEnable())
                .globalOperationParameters(headers)
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
