package com.example.mysqlbinlog.service.transform;

import com.example.mysqlbinlog.mode.TableColumnInfo;
import com.example.mysqlbinlog.service.transform.iface.TransformIface;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Service
public class EventDataToMapService implements TransformIface {

    @Override
    public Map<String, Object> transformToMap(Serializable[] values, List<TableColumnInfo> columnInfos) {
        return null;
    }
}
