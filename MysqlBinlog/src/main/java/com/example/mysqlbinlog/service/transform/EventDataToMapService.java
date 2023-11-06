package com.example.mysqlbinlog.service.transform;

import com.example.mysqlbinlog.model.TableColumnInfo;
import com.example.mysqlbinlog.service.transform.iface.TransformIface;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EventDataToMapService implements TransformIface {

    @Override
    public Map<String, Object> transformToMap(Serializable[] values, List<TableColumnInfo> columnInfos) {
        HashMap<String, Object> result = Maps.newHashMap();
        for (TableColumnInfo columnInfo : columnInfos) {
            String columnName = columnInfo.getColumnName();
            int index = columnInfo.getOrdinalPosition();
            Serializable value = values[index];

        }


        return result;
    }

}
