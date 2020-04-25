package com.linchtech.boot.starter.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.linchtech.boot.starter.common.HttpResult;
import com.linchtech.boot.starter.common.ResultVO;
import com.linchtech.boot.starter.properties.HttpProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import springfox.documentation.service.ApiInfo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.LongAdder;

/**
 *
 * @author 107
 * <p> Create at2019/9/26 10:36
 */
@Slf4j
public class HttpRestrictInterceptor implements HandlerInterceptor {

    private HttpProperties httpProperties;

    private ObjectMapper objectMapper;

    // FIXME: 2019/9/26 async http should change logic to fit two request match one response
    private Map<String, LongAdder> mappingCountMap = new ConcurrentHashMap<>();

    private ThreadLocal<List<String>> matchedMappingGroups = new ThreadLocal<>();

    public HttpRestrictInterceptor(HttpProperties httpProperties, ObjectMapper objectMapper) {
        this.httpProperties = httpProperties;
        this.objectMapper = objectMapper;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        if (CollectionUtils.isEmpty(httpProperties.getRestricts())) {
            return true;
        }
        String url = request.getRequestURI();
        String method = request.getMethod();
        String mapping = String.format("%s_%s", method == null ? "UNKNOWN": method.toUpperCase(), url == null ? "/" : url);

        if (log.isDebugEnabled()) {
            log.debug("mapping restrict mapping: [{}]", mapping);
        }

        List<String> groupIds = new ArrayList<>();
        final boolean[] result = new boolean[]{true};
        httpProperties.getRestricts().forEach((group, restrict) -> {
            if (result[0] && restrict.getMappingPatterns().stream().anyMatch(i -> Objects.nonNull(i) && mapping.matches(i))) {
                if (log.isDebugEnabled()) {
                    log.debug("Mapping restrict mapping matched: group key [{}], mapping [{}]", group, mapping);
                }
                LongAdder longAdder;
                if (mappingCountMap.containsKey(group)) {
                    longAdder = mappingCountMap.get(group);
                } else {
                    longAdder = new LongAdder();
                    LongAdder tmpLongAdder = mappingCountMap.putIfAbsent(group, longAdder);
                    if (tmpLongAdder != null && tmpLongAdder.hashCode() != longAdder.hashCode()) {
                        longAdder = tmpLongAdder;
                    }
                }
                if (longAdder.longValue() < restrict.getLimit()) {
                    longAdder.increment();
                    groupIds.add(group);
                } else {
                    result[0] = false;
                }
            }
        });

        if (result[0]) {
            matchedMappingGroups.set(groupIds);
        } else {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json; charset=utf-8");
            PrintWriter out = null ;
            try{
                ResultVO responseResult = new ResultVO();
                responseResult.setCode(HttpResult.SYSTEM_ERROR.getCode());


                // Copy trackId
                Map trackMap = (HashMap) request.getAttribute(WebInterceptor.TRACK_PARAM);
                if (!CollectionUtils.isEmpty(trackMap)) {
                    @SuppressWarnings("unchecked")
                    long startTimeMills = (long) trackMap.getOrDefault(WebInterceptor.PARAM_KEY_START, 0L);
                    @SuppressWarnings("unchecked")
                    String trackId = trackMap.getOrDefault(WebInterceptor.PARAM_KEY_TRACK_ID, "")
                            .toString();
                    //noinspection unchecked
                    trackMap.put(WebInterceptor.PARAM_KEY_TRACK_ID, String.format("%s_%s", trackId, "Request limit reject"));
                    double spentSec = ((double) (System.currentTimeMillis() - startTimeMills)) / 1000;
                    response.addHeader(WebInterceptor.PARAM_KEY_TRACK_ID, trackId);
                }
                out = response.getWriter();
                out.append(objectMapper.writeValueAsString(responseResult));
            }
            catch (Exception e){
                log.warn("Error info response exception: ", e);
                response.sendError(500);
            }
        }
        return result[0];
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        try {
            List<String> groups = matchedMappingGroups.get();
            if (!CollectionUtils.isEmpty(groups)) {
                groups.forEach(i -> {
                    LongAdder longAdder = mappingCountMap.getOrDefault(i, new LongAdder());
                    longAdder.decrement();
                    if (log.isDebugEnabled()) {
                        log.debug("API removed group key [{}], now count [{}]", i, longAdder.longValue());
                    }
                });
            }
        } finally {
            matchedMappingGroups.remove();
        }
    }
}
