package com.example.mysqlbinlog;

import com.example.mysqlbinlog.config.DataSyncElasticSearchConfig;
import com.example.mysqlbinlog.manager.EsClientManager;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.time.StopWatch;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.MatchAllQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;
import org.elasticsearch.index.reindex.UpdateByQueryRequest;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@SpringBootTest
public class EsTests {
    @Resource
    private EsClientManager esClientManager;
    private final DataSyncElasticSearchConfig config = new DataSyncElasticSearchConfig("localhost", 9200);

    private RequestOptions defaultOpt = RequestOptions.DEFAULT;

    @Test
    void testClient() throws IOException {
        RestHighLevelClient client = esClientManager.getClientByConfig(config);
        IndexRequest indexRequest = new IndexRequest("testtable");
        Map<String, Object> source = Maps.newHashMap();
        indexRequest.source(source);
        System.out.println(client.index(indexRequest, defaultOpt));
    }

    @Test
    void testSearch() throws IOException {
        RestHighLevelClient client = esClientManager.getClientByConfig(config);
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        SearchRequest searchRequest = new SearchRequest("testtable");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = client.search(searchRequest, defaultOpt);
        stopWatch.stop();
        System.out.println(stopWatch.getTime(TimeUnit.MILLISECONDS));

        System.out.println(searchResponse);
    }

    @Test
    void testAdd() throws IOException {
        RestHighLevelClient client = esClientManager.getClientByConfig(config);
        BulkRequest bulkRequest=new BulkRequest("testtable");

        IndexRequest indexRequest1 = new IndexRequest();
        Map<String, Serializable> source1 = new HashMap<>();
        source1.put("id", 1);
        source1.put("msg", "msg");
        Map<String, Serializable> source2 = new HashMap<>();
        source2.put("id", 2);
        source2.put("msg", "gsm");


        indexRequest1.source(source1);
        bulkRequest.add(indexRequest1);
        IndexRequest request2=new IndexRequest();
        request2.source(source2);
        bulkRequest.add(request2);


        System.out.println(client.bulk(bulkRequest, defaultOpt));
    }

    @Test
    void testUpdate() throws IOException {
        RestHighLevelClient client = esClientManager.getClientByConfig(config);
        UpdateByQueryRequest update = new UpdateByQueryRequest("testtable");
        update.setQuery(new TermQueryBuilder("id", "1"));


        System.out.println(client.updateByQuery(update, defaultOpt));

    }

    @Test
    void testDelete() throws IOException {
        RestHighLevelClient client = esClientManager.getClientByConfig(config);
        DeleteByQueryRequest delete = new DeleteByQueryRequest("testtable");
        delete.setQuery(new MatchAllQueryBuilder());
        System.out.println(client.deleteByQuery(delete, defaultOpt));
    }
}
