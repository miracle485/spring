package com.example.mysqlbinlog.service.handler;

import com.example.mysqlbinlog.config.DataSyncTasks;
import com.example.mysqlbinlog.manager.EsClientManager;
import com.example.mysqlbinlog.model.TableColumnInfo;
import com.example.mysqlbinlog.service.handler.iface.EventHandler;
import com.example.mysqlbinlog.service.sink.ElasticSearchService;
import com.example.mysqlbinlog.service.transform.EventDataTransformService;
import com.github.shyiko.mysql.binlog.event.EventData;
import com.github.shyiko.mysql.binlog.event.WriteRowsEventData;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class WriteEventHandler implements EventHandler {
    @Resource
    private EventDataTransformService dataTransformService;
    @Resource
    private ElasticSearchService elasticSearchService;


    @Override
    public void handleEvent(EventData data, List<TableColumnInfo> tableColumnInfos) {
        WriteRowsEventData writeRowsEventData = (WriteRowsEventData) data;
        List<Serializable[]> rows = writeRowsEventData.getRows();
        for (Serializable[] row : rows) {
            if (ArrayUtils.isEmpty(row)){
                continue;
            }
            Map<String, Serializable> dataMap = dataTransformService.transformToMap(row, tableColumnInfos);

            elasticSearchService.writeNewDataSingle(dataMap,"localhost",9200);
        }
    }
}
