package com.example.mysqlbinlog.service;

import com.example.mysqlbinlog.config.SourceMySqlConfig;
import com.example.mysqlbinlog.dao.source.SourceDataMapper;
import com.example.mysqlbinlog.model.TableColumInfo;
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
    @Resource
    private SourceDataMapper informationMapper;
    @Resource
    private SourceMySqlConfig mySqlConfig;

    private BinaryLogClient client;


    public void start() {
        List<String> databaseTable = loadTableNameFromDataBase("test");
        List<TableColumInfo> columns = loadTableColumnsFromDataBase(databaseTable);
        Map<String, List<TableColumInfo>> columnNameMap = columns.stream().map(TableColumInfo::tableNameToLower).collect(Collectors.groupingBy(TableColumInfo::getTableName));
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
        List<String> tableListByDatabaseName = informationMapper.getTableListByDatabaseName(dataBaseName);
        if (CollectionUtils.isEmpty(tableListByDatabaseName)) {
            return Lists.newArrayList();
        }

        return tableListByDatabaseName;
    }

    public List<TableColumInfo> loadTableColumnsFromDataBase(List<String> tableNameList) {
        if (CollectionUtils.isEmpty(tableNameList)) {
            return Lists.newArrayList();
        }
        List<TableColumInfo> tableInfoByTableList = informationMapper.getTableInfoByTableList(tableNameList);


        if (CollectionUtils.isEmpty(tableInfoByTableList)) {
            tableInfoByTableList = Lists.newArrayList();
        }

        return tableInfoByTableList;
    }

    public void disConnect() throws IOException {
        client.disconnect();
    }
}
