package com.example.mysqlbinlog.service.listener;

import com.example.mysqlbinlog.mode.TableColumInfo;
import com.github.shyiko.mysql.binlog.BinaryLogClient;
import com.github.shyiko.mysql.binlog.event.*;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.List;
import java.util.Map;

public class BinlogListener implements BinaryLogClient.EventListener {
    private final Gson gson = new Gson();
    private Map<Long, String> tableIdMap;
    private Map<String, List<TableColumInfo>> columnNameMap;

    public BinlogListener(Map<String, List<TableColumInfo>> columnNameMap) {
        tableIdMap = Maps.newConcurrentMap();
        this.columnNameMap = columnNameMap;
    }

    @Override
    public void onEvent(Event event) {
        EventType eventType = event.getHeader().getEventType();
        System.out.println(event.getHeader().getTimestamp() + "     " + eventType);
        if (EventType.isWrite(eventType)) {
            WriteRowsEventData data = event.getData();
            System.out.println(gson.toJson(data));
        }
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
            System.out.println(tableName);
        }
        //获取修改数据的记录
        if (EventType.isUpdate(eventType)) {
            UpdateRowsEventData updateRowsEventData = event.getData();
            System.out.println(gson.toJson(updateRowsEventData));
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
