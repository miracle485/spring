package com.example.mysqlbinlog.manager;

import com.example.mysqlbinlog.config.DataSyncElasticSearchConfig;
import com.google.common.collect.Maps;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

@Service
public class EsClientManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(EsClientManager.class);
    private final Map<DataSyncElasticSearchConfig, RestHighLevelClient> restHighLevelClientMap = Maps.newConcurrentMap();

    public RestHighLevelClient getClientByUrl(DataSyncElasticSearchConfig elasticSearchConfig) {
        if (elasticSearchConfig == null || StringUtils.isEmpty(elasticSearchConfig.getHost())) {
            return null;
        }
        RestHighLevelClient restHighLevelClient = MapUtils.getObject(restHighLevelClientMap, elasticSearchConfig);
        if (restHighLevelClient == null) {
            restHighLevelClient = createElasticSearchClient(elasticSearchConfig);
        }

        return restHighLevelClient;
    }

    private synchronized RestHighLevelClient createElasticSearchClient(DataSyncElasticSearchConfig elasticSearchConfig) {


        if (restHighLevelClientMap.containsKey(elasticSearchConfig)) {
            return restHighLevelClientMap.get(elasticSearchConfig);
        }
        HttpHost http = new HttpHost(elasticSearchConfig.getHost(), elasticSearchConfig.getPort(), "http");
        RestClientBuilder builder = RestClient.builder(http);
        //如果配置信息中的认证信息不为空，那么添加认证信息
        if (!StringUtils.isEmpty(elasticSearchConfig.getUserName())) {
            BasicCredentialsProvider credentialsProvider = new BasicCredentialsProvider();
            credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(elasticSearchConfig.getUserName(), elasticSearchConfig.getPassword()));
            builder.setHttpClientConfigCallback(httpAsyncClientBuilder -> httpAsyncClientBuilder.setDefaultCredentialsProvider(credentialsProvider));
        }

        return new RestHighLevelClient(builder);
    }

    public void shutdownAll() {
        restHighLevelClientMap.forEach(this::shutdownWithKey);
    }

    public void shutdownByUrl(DataSyncElasticSearchConfig config) {
        shutdownWithKey(config, restHighLevelClientMap.get(config));
    }

    private void shutdownWithKey(DataSyncElasticSearchConfig key, RestHighLevelClient client) {
        if (client == null) {
            return;
        }
        try {
            client.close();
        } catch (IOException e) {
            LOGGER.error("error when close es client, config is {}", key);
        }
    }

}
