package com.example.mysqlbinlog.service.sink;

import com.example.mysqlbinlog.manager.EsClientManager;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.rest.RestStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.Map;

@Service
public class ElasticSearchService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ElasticSearchService.class);
    @Resource
    private EsClientManager esClientManager;
    private final RequestOptions options = RequestOptions.DEFAULT;


    public boolean writeNewDataSingle(Map<String, Serializable> data, String host, int port) {
        if (MapUtils.isEmpty(data) || StringUtils.isEmpty(host) || port < 0) {
            LOGGER.error("illegal param or config when write ES data is {},host is {},port is {}", data, host, port);
        }
        RestHighLevelClient client = esClientManager.getClientByUrl(host, port);

        IndexRequest addRequest = new IndexRequest("testtable");
        addRequest.source(data);
        try {
            IndexResponse response = client.index(addRequest, options);
            if (RestStatus.CREATED != response.status()) {
                return false;
            }
        } catch (Exception e) {
            LOGGER.error("error when write data to es,data is {},error is ", data, e);
        }
        return true;
    }

    private void checkNullValue(Map<String, Serializable> data) {
        for (String key : data.keySet()) {
            if (MapUtils.getObject(data, key) == null) {
                data.remove(key);
            }
        }
    }
}
