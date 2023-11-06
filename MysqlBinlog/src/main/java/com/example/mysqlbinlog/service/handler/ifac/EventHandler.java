package com.example.mysqlbinlog.service.handler.ifac;

import com.example.mysqlbinlog.model.TableColumnInfo;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface EventHandler {
    void handleEvent(List<Map.Entry<Serializable[], Serializable[]>> rows, List<TableColumnInfo> tableColumnInfos);
}
