package com.linchtech.boot.starter.config;

import com.alibaba.fastjson.JSON;
import com.linchtech.boot.starter.common.AccessUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 请求调用拦截器
 *
 * @since jdk1.8
 */
@Slf4j
@SuppressWarnings("unchecked")
public class WebInterceptor implements HandlerInterceptor {

    public static final String TRACK_PARAM = "track";
    public static final String PARAM_KEY_TRACK_ID = "track_id";
    public static final String PARAM_KEY_START = "start";


    public static final ThreadLocal<AccessUser> USER_INFO = new ThreadLocal<>();
    private static final String ACCESS_USER_INFO_HEADER = "access-user-info";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String trackId = request.getHeader(PARAM_KEY_TRACK_ID);
        if (StringUtils.isEmpty(trackId)) {
            Map<String, Object> trackMap = (Map<String, Object>) request.getAttribute(WebInterceptor.TRACK_PARAM);
            if (trackMap != null && trackMap.get(WebInterceptor.PARAM_KEY_TRACK_ID) != null) {
                trackId = trackMap.get(WebInterceptor.PARAM_KEY_TRACK_ID).toString();
            } else {
                trackId = UUID.randomUUID().toString().replace("-", "");
            }
        }

        if (log.isDebugEnabled()) {
            Enumeration<String> headers = request.getHeaderNames();
            StringBuilder headerLog = new StringBuilder();
            headerLog.append("========= headers start");
            headerLog.append(System.lineSeparator());
            while (headers.hasMoreElements()) {
                String header = headers.nextElement();
                headerLog.append(header);
                headerLog.append(" => ");
                headerLog.append(request.getHeader(header));
                headerLog.append(System.lineSeparator());
            }
            headerLog.append("========= headers end");
            log.debug(headerLog.toString());
        }

        Map map = new HashMap(4);
        long startTimeMills = System.currentTimeMillis();
        map.put(WebInterceptor.PARAM_KEY_START, startTimeMills);
        map.put(WebInterceptor.PARAM_KEY_TRACK_ID, trackId);
        request.setAttribute(WebInterceptor.TRACK_PARAM, map);
        if (log.isInfoEnabled()) {
            log.info("track_{} {}_{} {}", trackId, request.getMethod(), request.getRequestURI(),
                    startTimeMills);
        }
        // 用户信息
        AccessUser userInfo = AccessUser.builder().build();
        AccessUser accessUserInfo = JSON.parseObject(request.getHeader(ACCESS_USER_INFO_HEADER),
                AccessUser.class);
        if (accessUserInfo != null) {
            userInfo = AccessUser.builder()
                    .userId(accessUserInfo.getUserId())
                    .ip(accessUserInfo.getIp())
                    .build();
        }
        USER_INFO.set(userInfo);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        modifyResponseTrack(request, response);
    }

    public static String modifyResponseTrack(HttpServletRequest request, HttpServletResponse response) {
        Map map = (HashMap) request.getAttribute(WebInterceptor.TRACK_PARAM);
        String trackId = map.getOrDefault(WebInterceptor.PARAM_KEY_TRACK_ID, "").toString();

        if (log.isInfoEnabled()) {
            log.info("track: {} {}_{}?{} remote_{}:{} local_{}:{} requestContentLength:{} UA:{} status:{} {}",
                    trackId, request.getMethod(), request.getRequestURI(),
                    StringUtils.hasLength(request.getQueryString()) ? request.getQueryString() : "",
                    request.getRemoteAddr(), request.getRemotePort(), request.getLocalAddr(),
                    request.getLocalPort(), request.getContentLengthLong(),
                    request.getHeader("user-agent"), response.getStatus(),
                    System.currentTimeMillis());
        }
        return trackId;
    }
}
