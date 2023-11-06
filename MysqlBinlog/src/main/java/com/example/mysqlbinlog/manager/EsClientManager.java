package com.example.mysqlbinlog.manager;

import com.google.common.collect.Maps;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class EsClientManager {
    private final Map<String, RestHighLevelClient> restHighLevelClientMap = Maps.newConcurrentMap();


}
