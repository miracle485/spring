package com.example.mysqlbinlog.service.sink;

import com.example.mysqlbinlog.manager.EsClientManager;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RestHighLevelClient;
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


    public boolean writeNewDataSingle(Map<String, Serializable> data, String host, int port) {
        RestHighLevelClient client = esClientManager.getClientByUrl(host, port);

        IndexRequest indexRequest = new IndexRequest();

        return true;
    }
}
