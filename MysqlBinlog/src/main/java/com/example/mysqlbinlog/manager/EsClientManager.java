package com.example.mysqlbinlog.manager;

import com.google.common.collect.Maps;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpHost;
import org.apache.http.message.BasicHeader;
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
    private final Map<String, RestHighLevelClient> restHighLevelClientMap = Maps.newConcurrentMap();

    public RestHighLevelClient getClientByUrl(String host, int port) {
        if (StringUtils.isEmpty(host)) {
            return null;
        }
        RestHighLevelClient restHighLevelClient = MapUtils.getObject(restHighLevelClientMap, buildClientKey(host, port));
        if (restHighLevelClient == null) {
            restHighLevelClient = createElasticSearchClient(host, port);
        }

        return restHighLevelClient;
    }

    private synchronized RestHighLevelClient createElasticSearchClient(String host, int port) {
        String key = buildClientKey(host, port);
        if (restHighLevelClientMap.containsKey(key)) {
            return restHighLevelClientMap.get(key);
        }
        HttpHost http = new HttpHost(host, port, "http");
        return new RestHighLevelClient(RestClient.builder(http).setDefaultHeaders(getCompatibilityHeader()));
    }

    private String buildClientKey(String host, int port) {
        return host + ":" + port;
    }

    public void shutdownAll() {
        restHighLevelClientMap.forEach(this::shutdownWithKey);
    }

    public void shutdownByUrl(String host, int port) {
        String key = buildClientKey(host, port);
        shutdownWithKey(key, restHighLevelClientMap.get(key));
    }

    private void shutdownWithKey(String key, RestHighLevelClient client) {
        if (client == null) {
            return;
        }
        try {
            client.close();
        } catch (IOException e) {
            LOGGER.error("error when close es client, config is {}", key);
        }
    }

    private Header[] getCompatibilityHeader() {
        return new Header[]{new BasicHeader(HttpHeaders.ACCEPT, "application/vnd.elasticsearch+json;compatible-with=7"), new BasicHeader(HttpHeaders.CONTENT_TYPE, "application/vnd.elasticsearch+json;compatible-with=7")};
    }

}
