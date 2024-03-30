// package com.linchtech.boot.starter.config;
//
// import com.fasterxml.jackson.core.JsonProcessingException;
// import com.fasterxml.jackson.databind.ObjectMapper;
// import com.google.common.collect.Lists;
// import com.linchtech.boot.starter.common.entity.vo.ResultVO;
// import com.linchtech.boot.starter.properties.HttpProperties;
// import lombok.extern.slf4j.Slf4j;
// import org.springframework.boot.context.properties.EnableConfigurationProperties;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.core.MethodParameter;
// import org.springframework.http.MediaType;
// import org.springframework.http.ResponseEntity;
// import org.springframework.http.converter.HttpMessageConverter;
// import org.springframework.http.server.ServerHttpRequest;
// import org.springframework.http.server.ServerHttpResponse;
// import org.springframework.http.server.ServletServerHttpRequest;
// import org.springframework.util.CollectionUtils;
// import org.springframework.util.StringUtils;
// import org.springframework.web.bind.annotation.RestControllerAdvice;
// import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
// import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
// import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
//
// import java.util.HashMap;
// import java.util.Map;
// import java.util.Objects;
//
// /**
//  * 自定义response body的配置返回信息
//  */
// @Slf4j
// @Configuration
// @RestControllerAdvice
// @EnableConfigurationProperties(HttpProperties.class)
// public class CustomResponseConfiguration implements ResponseBodyAdvice<Object>, WebMvcConfigurer {
//
//     private ObjectMapper objectMapper;
//
//     private HttpProperties rewriteProperties;
//
//     @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
//     public CustomResponseConfiguration(ObjectMapper objectMapper, HttpProperties rewriteProperties) {
//         this.objectMapper = objectMapper;
//         this.rewriteProperties = rewriteProperties;
//     }
//
//     @Override
//     public void addInterceptors(InterceptorRegistry registry) {
//         registry.addInterceptor(new WebInterceptor()).addPathPatterns("/**").excludePathPatterns("/**/swagger");
//     }
//
//     @Override
//     @SuppressWarnings("NullableProblems")
//     public boolean supports(MethodParameter returnParam,
//                             Class<? extends HttpMessageConverter<?>> converter) {
//         return null != returnParam.getMethod() && ResponseEntity.class != returnParam.getMethod().getReturnType();
//     }
//
//     @Override
//     @SuppressWarnings({"unchecked", "NullableProblems"})
//     public Object beforeBodyWrite(Object body,
//                                   MethodParameter returnType,
//                                   MediaType selectedContentType,
//                                   Class<? extends HttpMessageConverter<?>> selectedConverterType,
//                                   ServerHttpRequest request,
//                                   ServerHttpResponse response) {
//         if (!shouldRewrite(returnType, request)) {
//             return body;
//         }
//
//         ResultVO<?> finalResult;
//         Class<?> bodyClass = body != null ? body.getClass() : null;
//         if (null == body) {
//             finalResult = ResultVO.ok(null);
//             bodyClass = returnType.getParameterType();
//         } else if (bodyClass == ResultVO.class) {
//             finalResult = (ResultVO) body;
//         } else {
//             finalResult = ResultVO.ok(body);
//         }
//
//         // Copy trackId
//         Map trackMap = (HashMap) ((ServletServerHttpRequest) request).getServletRequest()
//                 .getAttribute(WebInterceptor.TRACK_PARAM);
//         if (!CollectionUtils.isEmpty(trackMap)) {
//             @SuppressWarnings("DuplicatedCode")
//             long startTimeMills = (long) trackMap.getOrDefault(WebInterceptor.PARAM_KEY_START, 0L);
//             String trackId = trackMap.getOrDefault(WebInterceptor.PARAM_KEY_TRACK_ID, "")
//                     .toString();
//             double spentSec = ((double) (System.currentTimeMillis() - startTimeMills)) / 1000;
//             response.getHeaders().put(WebInterceptor.PARAM_KEY_TRACK_ID, Lists.newArrayList(trackId));
//         }
//
//         try {
//             response.getHeaders().add("content-type", "application/json;charset=UTF-8");
//             return String.class == bodyClass ? objectMapper.writeValueAsString(finalResult) : finalResult;
//         } catch (JsonProcessingException e) {
//             log.error("body beforeBodyWrite error {}", finalResult);
//             throw new RuntimeException("系统异常");
//         }
//     }
//
//     private boolean shouldRewrite(MethodParameter returnType, ServerHttpRequest request) {
//         Class declaringClass = returnType.getDeclaringClass();
//         String className = declaringClass.getCanonicalName();
//         boolean result = true;
//
//         if (!CollectionUtils.isEmpty(rewriteProperties.getRewrite().getExcludePackages())) {
//             result = rewriteProperties.getRewrite().getExcludePackages().stream().filter(StringUtils::hasLength).noneMatch(className::startsWith);
//         }
//
//         if (result && !CollectionUtils.isEmpty(rewriteProperties.getRewrite().getExcludeClasses())) {
//             result = rewriteProperties.getRewrite().getExcludeClasses().stream().filter(Objects::nonNull).noneMatch(i -> declaringClass == i);
//         }
//
//         if (result && !CollectionUtils.isEmpty(rewriteProperties.getRewrite().getExcludePaths())) {
//             String url = String.format("%s_%s", request.getMethod(), request.getURI().getPath());
//             result = rewriteProperties.getRewrite().getExcludePaths().stream().filter(StringUtils::hasLength).noneMatch(i -> url.matches(i.replaceAll(
//                     "\\{[^/{}]+}", "[^/]+")));
//         }
//         return result;
//     }
// }
