package com.example.mysqlbinlog.service.handler;

import com.example.mysqlbinlog.model.TableColumnInfo;
import com.example.mysqlbinlog.service.handler.iface.EventHandler;
import com.github.shyiko.mysql.binlog.event.EventData;
import com.github.shyiko.mysql.binlog.event.UpdateRowsEventData;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UpdateEventHandler implements EventHandler {
    @Override
    public void handleEvent(EventData data, List<TableColumnInfo> tableColumnInfos) {
        UpdateRowsEventData updateRowsEventData = ((UpdateRowsEventData) data);

        // rows 每一个 Entry 是条记录,其中 Key 为修改前的记录，Value 为修改后的新的记录
        List<Map.Entry<Serializable[], Serializable[]>> rows = updateRowsEventData.getRows();
        // 获取修改后的新的值
        List<Serializable[]> newValues = rows.stream().map(Map.Entry::getValue).collect(Collectors.toList());
        // 获取修改前的值
        List<Serializable[]> oldValues = rows.stream().map(Map.Entry::getKey).collect(Collectors.toList());

    }
}
