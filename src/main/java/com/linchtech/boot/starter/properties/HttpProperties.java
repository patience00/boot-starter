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
@ConfigurationProperties(prefix = "linch.http")
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
    private List<CorsMapping> corsMappings = new ArrayList<>();

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

    @Data
    public static class CorsMapping {

        /**
         * Enable cross-origin request handling for the specified path pattern.
         *
         * <p>Exact path mapping URIs (such as {@code "/admin"}) are supported as
         * well as Ant-style path patterns (such as {@code "/admin/**"}).
         */
        private String mappingPattern;

        /**
         *
         * Set the HTTP methods to allow, e.g. {@code "GET"}, {@code "POST"},
         * {@code "PUT"}, etc.
         * <p>The special value {@code "*"} allows all methods.
         * <p>If not set, only {@code "GET"} and {@code "HEAD"} are allowed.
         * <p>By default this is not set.
         */
        private List<String> allowedMethods;

        /**
         *
         * The list of allowed origins that be specific origins, e.g.
         * {@code "http://domain1.com"}, or {@code "*"} for all origins.
         * <p>A matched origin is listed in the {@code Access-Control-Allow-Origin}
         * response header of preflight actual CORS requests.
         * <p>By default, all origins are allowed.
         */
        private List<String> allowedOrigins;

        /**
         * Set the list of headers that a preflight request can list as allowed
         * for use during an actual request. The special value {@code "*"} may be
         * used to allow all headers.
         * <p>A header name is not required to be listed if it is one of:
         * {@code Cache-Control}, {@code Content-Language}, {@code Expires},
         * {@code Last-Modified}, or {@code Pragma} as per the CORS spec.
         * <p>By default all headers are allowed.
         */
        private List<String> allowedHeaders;

        /**
         * Configure how long in seconds the response from a pre-flight request
         * can be cached by clients.
         * <p>By default this is set to 1800 seconds (30 minutes).
         */
        private long maxAgeSeconds = 1800;

        /**
         * Whether the browser should send credentials, such as cookies along with
         * cross domain requests, to the annotated endpoint. The configured value is
         * set on the {@code Access-Control-Allow-Credentials} response header of
         * preflight requests.
         * <p><strong>NOTE:</strong> Be aware that this option establishes a high
         * level of trust with the configured domains and also increases the surface
         * attack of the web application by exposing sensitive user-specific
         * information such as cookies and CSRF tokens.
         * <p>By default this is not set in which case the
         * {@code Access-Control-Allow-Credentials} header is also not set and
         * credentials are therefore not allowed.
         */
        private Boolean allowCredentials;
    }
}
