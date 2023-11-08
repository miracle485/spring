package com.example.mysqlbinlog.service.transform.iface;

import com.example.mysqlbinlog.model.TableColumnInfo;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface TransformIface {
    Map<String, Serializable> transformToMap(Serializable[] values, List<TableColumnInfo> columnInfos);

    List<Serializable> transformToList(Serializable[] values);

    /**
     * 对于text和blob类型的数据，会被解析为
     */
    Serializable[] transformDataTye(Serializable[] values);
}
