package com.example.mysqlbinlog.service.listener;

import com.example.mysqlbinlog.model.TableColumnInfo;
import com.example.mysqlbinlog.service.BeanUtilService;
import com.example.mysqlbinlog.service.handler.DeleteEventHandler;
import com.example.mysqlbinlog.service.handler.UpdateEventHandler;
import com.example.mysqlbinlog.service.handler.WriteEventHandler;
import com.github.shyiko.mysql.binlog.BinaryLogClient;
import com.github.shyiko.mysql.binlog.event.*;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.Gson;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class BinlogListener implements BinaryLogClient.EventListener {
    private final Gson gson = new Gson();
    private Map<Long, String> tableIdMap;
    private Map<String, List<TableColumnInfo>> columnNameMap;
    private Set<String> originTableName = Sets.newHashSet("testtable");

    public BinlogListener(Map<String, List<TableColumnInfo>> columnNameMap) {
        tableIdMap = Maps.newConcurrentMap();
        this.columnNameMap = columnNameMap;
    }

    @Override
    public void onEvent(Event event) {
        EventType eventType = event.getHeader().getEventType();
        //记录tableid和表名的映射关系
        if (EventType.TABLE_MAP.equals(eventType)) {
            TableMapEventData eventData = event.getData();
            String tableName = eventData.getTable();
            Long tableId = eventData.getTableId();
            loadTableId(tableId, tableName);
        }
        //获取新增数据的记录
        if (EventType.isWrite(eventType)) {
            WriteRowsEventData writeRowsEventData = event.getData();
            String tableName = MapUtils.getString(tableIdMap, writeRowsEventData.getTableId());
            //只获取源表的变化
            if (!originTableName.contains(tableName)) {
                return;
            }

            WriteEventHandler handler = BeanUtilService.getBean(WriteEventHandler.class);
            handler.handleEvent(writeRowsEventData, MapUtils.getObject(columnNameMap, tableName));

        }
        //获取修改数据的记录
        if (EventType.isUpdate(eventType)) {
            UpdateRowsEventData updateRowsEventData = event.getData();
            String tableName = MapUtils.getString(tableIdMap, updateRowsEventData.getTableId());
            //只获取源表的变化
            if (!originTableName.contains(tableName)) {
                return;
            }
            UpdateEventHandler handler = BeanUtilService.getBean(UpdateEventHandler.class);
            handler.handleEvent(updateRowsEventData, MapUtils.getObject(columnNameMap, tableName));
        }

        //获取删除数据的记录
        if (EventType.isDelete(eventType)) {
            DeleteRowsEventData data = event.getData();
            String tableName = MapUtils.getString(tableIdMap, data.getTableId());
            //只获取源表的变化
            if (!originTableName.contains(tableName)) {
                return;
            }
            DeleteEventHandler handler = BeanUtilService.getBean(DeleteEventHandler.class);
            handler.handleEvent(data, MapUtils.getObject(columnNameMap, tableName));
        }

    }

    private void loadTableId(Long tableId, String tableName) {
        if (tableId == null || StringUtils.isEmpty(tableName)) {
            return;
        }
        if (!MapUtils.getString(tableIdMap, tableId, StringUtils.EMPTY).equals(tableName)) {
            tableIdMap.put(tableId, tableName);
        }
    }

}
