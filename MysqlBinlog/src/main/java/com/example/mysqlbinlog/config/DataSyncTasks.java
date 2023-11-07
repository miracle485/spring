package com.example.mysqlbinlog.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties(prefix = "data-sync.config.target")
public class DataSyncTasks {
    @Value("${elasticSearchConfigList}")
    private List<DataSyncElasticSearchConfig> elasticSearchConfigList;
    private List<DataSyncMysqlTaskConfig> mysqlTaskConfigList;


}
