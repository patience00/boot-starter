package com.linchtech.boot.starter.config;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;

import java.time.Duration;

/**
 * @author 107
 * @date 2020-05-23 16:08
 * @description
 **/
@Configuration
@ConfigurationProperties(prefix = "spring.elasticsearch.rest")
@ConditionalOnClass(RestHighLevelClient.class)
public class ElasticSearchConfig extends AbstractElasticsearchConfiguration {

    private String uris;
    private String username;
    private String password;

    @Override
    public RestHighLevelClient elasticsearchClient() {
        ClientConfiguration clientConfiguration = ClientConfiguration.builder()
                .connectedTo(uris)
                .withConnectTimeout(Duration.ofSeconds(5))
                .withSocketTimeout(Duration.ofSeconds(3))
                .withBasicAuth(username, password).build();
        return RestClients.create(clientConfiguration).rest();
    }
}
