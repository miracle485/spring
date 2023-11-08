package com.example.mysqlbinlog.config;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
//@PropertySource("classpath:datasynctasks.yml")
@ConfigurationProperties(prefix = "data-sync.config.target")
public class DataSyncTasks {
    private List<DataSyncElasticSearchConfig> elasticSearchConfigList;
    private List<DataSyncMysqlTaskConfig> mysqlTaskConfigList;

    public List<DataSyncElasticSearchConfig> getElasticSearchConfigList() {
        return elasticSearchConfigList;
    }

    public void setElasticSearchConfigList(List<DataSyncElasticSearchConfig> elasticSearchConfigList) {
        this.elasticSearchConfigList = elasticSearchConfigList;
    }

    public List<DataSyncMysqlTaskConfig> getMysqlTaskConfigList() {
        return mysqlTaskConfigList;
    }

    public void setMysqlTaskConfigList(List<DataSyncMysqlTaskConfig> mysqlTaskConfigList) {
        this.mysqlTaskConfigList = mysqlTaskConfigList;
    }

    public void removeInvalidConfig() {
        if (!CollectionUtils.isEmpty(elasticSearchConfigList)) {
            elasticSearchConfigList.removeIf(DataSyncElasticSearchConfig::isInValidConfig);
        }

        if (!CollectionUtils.isEmpty(mysqlTaskConfigList)) {
            mysqlTaskConfigList.removeIf(DataSyncMysqlTaskConfig::isInValidConfig);
        }
    }
}
