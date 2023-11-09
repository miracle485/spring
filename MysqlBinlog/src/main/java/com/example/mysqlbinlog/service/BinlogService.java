package com.example.mysqlbinlog.service;

import com.example.mysqlbinlog.config.SourceMySqlConfig;
import com.example.mysqlbinlog.dao.source.SourceDataMapper;
import com.example.mysqlbinlog.manager.DataSourceManager;
import com.example.mysqlbinlog.model.TableColumnInfo;
import com.example.mysqlbinlog.service.listener.BinlogListener;
import com.github.shyiko.mysql.binlog.BinaryLogClient;
import com.github.shyiko.mysql.binlog.event.deserialization.EventDeserializer;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BinlogService {
    private static final Logger LOGGER = LoggerFactory.getLogger(BinlogService.class);
    private SourceDataMapper sourceDataMapper;
    @Resource
    private SourceMySqlConfig mySqlConfig;
    @Resource
    private DataSourceManager dataSourceManager;

    private BinaryLogClient client;


    public void start() {
        sourceDataMapper = dataSourceManager.getSourceMapper(mySqlConfig);

        List<String> databaseTable = loadTableNameFromDataBase("test");
        List<TableColumnInfo> columns = loadTableColumnsFromDataBase(databaseTable);
        Map<String, List<TableColumnInfo>> columnNameMap = columns.stream().map(TableColumnInfo::tableNameToLower).collect(Collectors.groupingBy(TableColumnInfo::getTableName));
        BinlogListener binlogListener = new BinlogListener(columnNameMap);
        startListener(binlogListener);
    }


    private void startListener(BinaryLogClient.EventListener listener) {
        EventDeserializer eventDeserializer = new EventDeserializer();
        client = new BinaryLogClient(mySqlConfig.getHost(), mySqlConfig.getPort(), mySqlConfig.getUserName(), mySqlConfig.getPassword());
        client.setEventDeserializer(eventDeserializer);
        client.registerEventListener(listener);


        try {
            client.connect();
        } catch (IOException e) {
            LOGGER.error("error when connect server, error is ", e);
        } finally {

        }
    }

    public List<String> loadTableNameFromDataBase(String dataBaseName) {
        List<String> tableListByDatabaseName = sourceDataMapper.getTableListByDatabaseName(dataBaseName);
        if (CollectionUtils.isEmpty(tableListByDatabaseName)) {
            return Lists.newArrayList();
        }

        return tableListByDatabaseName;
    }

    public List<TableColumnInfo> loadTableColumnsFromDataBase(List<String> tableNameList) {
        if (CollectionUtils.isEmpty(tableNameList)) {
            return Lists.newArrayList();
        }
        List<TableColumnInfo> tableInfoByTableList = sourceDataMapper.getTableInfoByTableList(tableNameList);


        if (CollectionUtils.isEmpty(tableInfoByTableList)) {
            tableInfoByTableList = Lists.newArrayList();
        }

        return tableInfoByTableList;
    }

    public void disConnect() throws IOException {
        client.disconnect();
        LOGGER.info("binlog client has bean shutdown");
    }
}
