package com.example.mysqlbinlog.service;

import com.example.mysqlbinlog.dao.InformationMapper;
import com.example.mysqlbinlog.mode.TableColumInfo;
import com.example.mysqlbinlog.service.listener.BinlogListener;
import com.github.shyiko.mysql.binlog.BinaryLogClient;
import com.github.shyiko.mysql.binlog.event.deserialization.EventDeserializer;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BinlogService {
    @Resource
    private InformationMapper informationMapper;
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
        client = new BinaryLogClient("localhost", 3306, "root", "MySql2023!");
        client.setEventDeserializer(eventDeserializer);
        client.registerEventListener(listener);

        try {
            client.connect();
        } catch (IOException e) {
            throw new RuntimeException(e);
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
