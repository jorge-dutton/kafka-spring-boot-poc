package com.jdutton.poc.elastic.config;

import com.jdutton.poc.config.ElasticConfigData;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@Configuration
@EnableElasticsearchRepositories(basePackages = "com.jdutton.poc.elastic.index.client.repository" )
public class ElasticsearchConfig extends AbstractElasticsearchConfiguration {
    private ElasticConfigData elasticConfigData;

    public ElasticsearchConfig(ElasticConfigData elasticConfigData) {
        this.elasticConfigData = elasticConfigData;
    }

    @Override
    @Bean
    public RestHighLevelClient elasticsearchClient() {
        UriComponents serverUri = UriComponentsBuilder
                .fromHttpUrl(elasticConfigData.getConnectionUrl())
                .userInfo("elastic:UIryN35bKozUTDwCnyEdY0lv").build();
        return new RestHighLevelClient(
                RestClient.builder(new HttpHost(
                serverUri.getHost(),
                serverUri.getPort(),
                serverUri.getScheme()
            )).setRequestConfigCallback(
                requestConfigBuilder ->
                        requestConfigBuilder
                                .setAuthenticationEnabled(true)
                                .setConnectTimeout(elasticConfigData.getConnectTimeoutMs())
                                .setSocketTimeout(elasticConfigData.getSocketTimeoutMs())
            )
        );
    }

    @Bean
    public ElasticsearchOperations elasticsearchTemplate() {
        return new ElasticsearchRestTemplate(elasticsearchClient());
    }
}
