package com.example.mysqlbinlog.service.sink;

import com.example.mysqlbinlog.config.DataSyncMysqlTaskConfig;
import com.example.mysqlbinlog.dao.target.TargetWriteMapper;
import com.example.mysqlbinlog.manager.DataSourceManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;

@Service
public class DataSourceService {
    private static final Logger LOGGER = LoggerFactory.getLogger(DataSourceService.class);

    @Resource
    private DataSourceManager dataSourceManager;

    public boolean writeDataToTarget(List<Serializable[]> values, DataSyncMysqlTaskConfig config) {
        TargetWriteMapper targetMapper = dataSourceManager.getTargetMapper(config);
        if (targetMapper == null) {
            LOGGER.error("can't get DataSourceMapper, please check config, config is {}", config);
            return false;
        }
        String tableName = config.getTargetDataBaseName() + "." + config.getTargetTableName();

        int rowCount = targetMapper.syncWriteNewRows(tableName, values);


        return rowCount == values.size();
    }
}
