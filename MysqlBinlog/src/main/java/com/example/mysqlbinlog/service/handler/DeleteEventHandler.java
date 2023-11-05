package com.example.mysqlbinlog.service.handler;

import com.example.mysqlbinlog.mode.TableColumnInfo;
import com.example.mysqlbinlog.service.handler.ifac.EventHandler;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Service
public class DeleteEventHandler implements EventHandler {
    @Override
    public void handleEvent(List<Map.Entry<Serializable[], Serializable[]>> rows, List<TableColumnInfo> tableColumnInfos) {

    }
}
