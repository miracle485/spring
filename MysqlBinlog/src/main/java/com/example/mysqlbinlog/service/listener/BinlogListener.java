package com.example.mysqlbinlog.service.listener;

import com.github.shyiko.mysql.binlog.BinaryLogClient;
import com.github.shyiko.mysql.binlog.event.*;
import com.google.gson.Gson;

public class BinlogListener implements BinaryLogClient.EventListener {
    private final Gson gson = new Gson();

    @Override
    public void onEvent(Event event) {
        EventType eventType = event.getHeader().getEventType();
        System.out.println(event.getHeader().getTimestamp() + "     " + eventType);
        if (EventType.isWrite(eventType)) {
            WriteRowsEventData data = event.getData();
            System.out.println(gson.toJson(data));
        }
        //查看tableid和表名的映射关系
        if (EventType.TABLE_MAP.equals(eventType)) {
            TableMapEventData eventData = event.getData();
            System.out.println(gson.toJson(eventData));
        }
    }
}
