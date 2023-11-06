package com.example.mysqlbinlog.service.listener;

import com.example.mysqlbinlog.dao.TargetWriteMapper;
import com.example.mysqlbinlog.mode.TableColumInfo;
import com.example.mysqlbinlog.service.BeanUtilService;
import com.github.shyiko.mysql.binlog.BinaryLogClient;
import com.github.shyiko.mysql.binlog.event.*;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.Gson;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class BinlogListener implements BinaryLogClient.EventListener {
    private final Gson gson = new Gson();
    private Map<Long, String> tableIdMap;
    private Map<String, List<TableColumInfo>> columnNameMap;
    private Set<String> originTableName = Sets.newHashSet("testtable");

    public BinlogListener(Map<String, List<TableColumInfo>> columnNameMap) {
        tableIdMap = Maps.newConcurrentMap();
        this.columnNameMap = columnNameMap;
    }

    @Override
    public void onEvent(Event event) {
        EventType eventType = event.getHeader().getEventType();
        System.out.println(event.getHeader().getTimestamp() + "     " + eventType);
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
            if (!originTableName.contains(tableName)) {
                return;
            }

            System.out.println(gson.toJson(writeRowsEventData.getRows()));

        }
        //获取修改数据的记录
        if (EventType.isUpdate(eventType)) {
            UpdateRowsEventData updateRowsEventData = event.getData();

            // rows 每一个 Entry 是条记录,其中 Key 为修改前的记录，Value 为修改后的新的记录
            List<Map.Entry<Serializable[], Serializable[]>> rows = updateRowsEventData.getRows();
            // 获取修改后的新的值
            List<Serializable[]> newValues = rows.stream().map(entry -> entry.getValue()).collect(Collectors.toList());
            // 获取修改前的值
            List<Serializable[]> oldValues = rows.stream().map(entry -> entry.getKey()).collect(Collectors.toList());

            for (Serializable[] newValue : newValues) {
                System.out.println("newValue  " + gson.toJson(newValue));
            }


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

    private List<Serializable> transformToMap(List<Serializable> rows, List<TableColumInfo> columInfos) {
        List<Serializable> result = Lists.newArrayList();
        for (Serializable row : rows) {
            Serializable value = row instanceof byte[] ? String.valueOf(row) : row;
            result.add(value);
        }

        return result;
    }
}
