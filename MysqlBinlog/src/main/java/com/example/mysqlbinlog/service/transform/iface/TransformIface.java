package com.example.mysqlbinlog.service.transform.iface;

import com.example.mysqlbinlog.model.TableColumnInfo;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface TransformIface {
    Map<String, Object> transformToMap(Serializable[] values, List<TableColumnInfo> columnInfos);

    List<Serializable> transformToList(Serializable[] values, List<TableColumnInfo> columnInfos);
}
