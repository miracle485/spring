package com.example.mysqlbinlog.service.transform;

import com.example.mysqlbinlog.model.TableColumnInfo;
import com.example.mysqlbinlog.service.transform.iface.TransformIface;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EventDataTransformService implements TransformIface {

    @Override
    public Map<String, Serializable> transformToMap(Serializable[] values, List<TableColumnInfo> columnInfos) {
        HashMap<String, Serializable> result = Maps.newHashMap();
        for (TableColumnInfo columnInfo : columnInfos) {
            String columnName = columnInfo.getColumnName();
            int index = columnInfo.getOrdinalPosition();
            Serializable value = values[index - 1] instanceof byte[] ? values[index - 1] : String.valueOf(values[index - 1]);
            result.put(columnName, value);
        }


        return result;
    }

    @Override
    public List<Serializable> transformToList(Serializable[] rows) {
        List<Serializable> result = Lists.newArrayList();
        for (Serializable row : rows) {
            Serializable value = row instanceof byte[] ? String.valueOf(row) : row;
            result.add(value);
        }

        return result;
    }

    @Override
    public Serializable[] transformDataTye(Serializable[] values) {
        for (int i = 0; i < values.length; i++) {
            if (values[i] instanceof byte[]) {
                values[i] = String.valueOf(values[i]);
            }
        }
        return values;
    }


}
