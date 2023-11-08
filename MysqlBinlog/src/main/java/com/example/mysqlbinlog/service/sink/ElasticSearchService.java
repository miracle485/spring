package com.example.mysqlbinlog.service.sink;

import com.example.mysqlbinlog.config.DataSyncElasticSearchConfig;
import com.example.mysqlbinlog.manager.EsClientManager;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.rest.RestStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Service
public class ElasticSearchService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ElasticSearchService.class);
    @Resource
    private EsClientManager esClientManager;
    private final RequestOptions options = RequestOptions.DEFAULT;


    public boolean writeNewDataSingle(Map<String, Serializable> data, DataSyncElasticSearchConfig config) {
        if (!checkParam(config)) {
            return false;
        }
        RestHighLevelClient client = esClientManager.getClientByConfig(config);

        IndexRequest addRequest = new IndexRequest("testtable");
        addRequest.source(data);
        try {
            IndexResponse response = client.index(addRequest, options);
            if (RestStatus.CREATED != response.status()) {
                return true;
            }
        } catch (Exception e) {
            LOGGER.error("error when write data to es,data is {},error is ", data, e);
            return false;
        }
        return true;
    }


    public boolean writeNewDataBatch(List<Map<String, Serializable>> values, DataSyncElasticSearchConfig config) {
        if (!checkParam(config) || CollectionUtils.isEmpty(values)) {
            return false;
        }
        RestHighLevelClient client = esClientManager.getClientByConfig(config);
        BulkRequest request = new BulkRequest(config.getIndexName());

        for (Map<String, Serializable> value : values) {
            IndexRequest indexRequest = new IndexRequest();
            indexRequest.source(value);
            request.add(indexRequest);
        }
        try {
            BulkResponse bulk = client.bulk(request, RequestOptions.DEFAULT);
            if (RestStatus.CREATED != bulk.status()) {
                return true;
            }
        } catch (IOException e) {
            LOGGER.error("error when write data to es,data is {},error is ", values, e);
            return false;
        }
        return true;
    }

    private boolean checkParam(DataSyncElasticSearchConfig config) {
        if (StringUtils.isEmpty(config.getHost()) || config.getPort() < 0) {
            LOGGER.error("illegal param or config when write ES ,host is {},port is {}", config.getHost(), config.getPort());
            return false;
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
