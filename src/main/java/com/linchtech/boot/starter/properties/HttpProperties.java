package com.linchtech.boot.starter.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * <p> Create at2019/3/14 13:19
 */
@Data
@ConfigurationProperties(prefix = "system.http")
public class HttpProperties {

    private Rewrite rewrite = new Rewrite();

    private Rest rest = new Rest();

    /**
     * Restrict limit list. `key` restricts name
     * </p> Format for mapping: METHOD_/path_your_api, eg. `GET_/api/v1/devices`
     * </p> Also mapping allowed regex pattern, eg. `GET_/api/v1/module1/.*` matched
     * all mapping start with `GET_/api/v1/module1/`
     */
    private Map<String, Restrict> restricts = new HashMap<>();


    @Data
    public static class Rewrite {
        private List<String> excludePackages;

        private List<Class> excludeClasses;

        private List<String> excludePaths;
    }

    @Data
    public static class Rest {
        private int connectTimeout = 5000;

        private int readTimeout = 5000;
    }

    @Data
    public static class Restrict {

        private List<String> mappingPatterns = new ArrayList<>();

        private Integer limit;
    }

}
