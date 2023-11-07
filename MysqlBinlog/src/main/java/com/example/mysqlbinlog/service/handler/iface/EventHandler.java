package com.example.mysqlbinlog.service.handler.iface;

import com.example.mysqlbinlog.model.TableColumnInfo;
import com.github.shyiko.mysql.binlog.event.EventData;

import java.util.List;

public interface EventHandler {
    void handleEvent(EventData data, List<TableColumnInfo> tableColumnInfos);
}
