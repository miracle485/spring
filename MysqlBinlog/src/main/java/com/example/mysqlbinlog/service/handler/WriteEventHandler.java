package com.example.mysqlbinlog.service.handler;

import com.example.mysqlbinlog.config.DataSyncElasticSearchConfig;
import com.example.mysqlbinlog.config.DataSyncMysqlTaskConfig;
import com.example.mysqlbinlog.config.DataSyncTasks;
import com.example.mysqlbinlog.model.TableColumnInfo;
import com.example.mysqlbinlog.service.handler.iface.EventHandler;
import com.example.mysqlbinlog.service.sink.DataSourceService;
import com.example.mysqlbinlog.service.sink.ElasticSearchService;
import com.example.mysqlbinlog.service.transform.EventDataTransformService;
import com.github.shyiko.mysql.binlog.event.Event;
import com.github.shyiko.mysql.binlog.event.EventData;
import com.github.shyiko.mysql.binlog.event.EventType;
import com.github.shyiko.mysql.binlog.event.WriteRowsEventData;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Service
public class WriteEventHandler implements EventHandler {
    @Resource
    private EventDataTransformService dataTransformService;
    @Resource
    private ElasticSearchService elasticSearchService;
    @Resource
    private DataSourceService dataSourceService;
    @Resource
    private DataSyncTasks tasks;


    @Override
    public void checkTypeAndHandleEvent(Event event, List<TableColumnInfo> tableColumnInfos) {
        if (!EventType.isWrite(event.getHeader().getEventType())) {
            return;
        }
        handleEvent(event.getData(), tableColumnInfos);
    }


    public void handleEvent(EventData data, List<TableColumnInfo> tableColumnInfos) {
        WriteRowsEventData writeRowsEventData = (WriteRowsEventData) data;
        List<Serializable[]> rows = writeRowsEventData.getRows();
        for (Serializable[] row : rows) {
            if (ArrayUtils.isEmpty(row)) {
                continue;
            }
            dataTransformService.transformDataTye(row);
        }
        runWriteTasks(rows, tableColumnInfos);
    }

    private void runWriteTasks(List<Serializable[]> rows, List<TableColumnInfo> tableColumnInfos) {
        List<Map<String, Serializable>> valueWithColumnMap = Lists.newArrayList();
        for (Serializable[] row : rows) {
            Map<String, Serializable> valueMap = dataTransformService.transformToMap(row, tableColumnInfos);
            if (MapUtils.isNotEmpty(valueMap)) {
                valueWithColumnMap.add(valueMap);
            }
        }

        List<DataSyncElasticSearchConfig> elasticSearchConfigList = tasks.getElasticSearchConfigList();
        for (DataSyncElasticSearchConfig config : elasticSearchConfigList) {
            elasticSearchService.writeNewDataBatch(valueWithColumnMap, config);
        }
        List<DataSyncMysqlTaskConfig> mysqlTaskConfigList = tasks.getMysqlTaskConfigList();
        for (DataSyncMysqlTaskConfig config : mysqlTaskConfigList) {
            System.out.println(dataSourceService.writeDataToTarget(rows, config));
        }


    }
}
