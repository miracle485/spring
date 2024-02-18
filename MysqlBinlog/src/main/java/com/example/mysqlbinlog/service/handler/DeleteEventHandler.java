package com.example.mysqlbinlog.service.handler;

import com.example.mysqlbinlog.model.TableColumnInfo;
import com.example.mysqlbinlog.service.handler.iface.EventHandler;
import com.github.shyiko.mysql.binlog.event.Event;
import com.github.shyiko.mysql.binlog.event.EventData;
import com.github.shyiko.mysql.binlog.event.EventType;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeleteEventHandler implements EventHandler {

    @Override
    public void checkTypeAndHandleEvent(Event event, List<TableColumnInfo> tableColumnInfos) {
        if (!EventType.isDelete(event.getHeader().getEventType())) {
            return;
        }
        handleEvent(event.getData(), tableColumnInfos);
    }

    public void handleEvent(EventData data, List<TableColumnInfo> tableColumnInfos) {

    }
}
