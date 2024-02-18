package com.example.mysqlbinlog.service.handler.iface;

import com.example.mysqlbinlog.model.TableColumnInfo;
import com.github.shyiko.mysql.binlog.event.Event;

import java.util.List;

public interface EventHandler {
    void checkTypeAndHandleEvent(Event data, List<TableColumnInfo> tableColumnInfos);
}
