package com.example.mysqlbinlog.service.handler;

import com.example.mysqlbinlog.model.TableColumnInfo;
import com.example.mysqlbinlog.service.handler.iface.EventHandler;
import com.github.shyiko.mysql.binlog.event.EventData;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeleteEventHandler implements EventHandler {
    @Override
    public void handleEvent(EventData data, List<TableColumnInfo> tableColumnInfos) {

    }
}
