package com.example.mysqlbinlog.service.listener;

import com.example.mysqlbinlog.mode.TableColumnInfo;
import com.github.shyiko.mysql.binlog.BinaryLogClient;
import com.github.shyiko.mysql.binlog.event.DeleteRowsEventData;
import com.github.shyiko.mysql.binlog.event.Event;
import com.github.shyiko.mysql.binlog.event.EventType;
import com.github.shyiko.mysql.binlog.event.TableMapEventData;
import com.github.shyiko.mysql.binlog.event.UpdateRowsEventData;
import com.github.shyiko.mysql.binlog.event.WriteRowsEventData;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BinlogListener implements BinaryLogClient.EventListener {
    private final Gson gson = new Gson();
    private Map<Long, String> tableIdMap;
    private Map<String, List<TableColumnInfo>> columnNameMap;

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

        }
        //获取修改数据的记录
        if (EventType.isUpdate(eventType)) {
            UpdateRowsEventData updateRowsEventData = event.getData();

            // rows 每一个 Entry 是条记录,其中 Key 为修改前的记录，Value 为修改后的新的记录
            List<Map.Entry<Serializable[], Serializable[]>> rows = updateRowsEventData.getRows();
            // 获取修改后的新的值
            List<Serializable[]> newValues = rows.stream().map(Map.Entry::getValue).collect(Collectors.toList());
            // 获取修改前的值
            List<Serializable[]> oldValues = rows.stream().map(Map.Entry::getKey).collect(Collectors.toList());

        }

        //获取删除数据的记录
        if (EventType.isDelete(eventType)) {
            DeleteRowsEventData data = event.getData();
            System.out.println(gson.toJson(data));
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
