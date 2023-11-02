package com.example.mysqlbinlog.service;

import com.example.mysqlbinlog.dao.InformationMapper;
import com.example.mysqlbinlog.mode.TableColumInfo;
import com.example.mysqlbinlog.service.listener.BinlogListener;
import com.github.shyiko.mysql.binlog.BinaryLogClient;
import com.github.shyiko.mysql.binlog.event.deserialization.EventDeserializer;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

public class BinlogService {
    public void startListener() {
        EventDeserializer eventDeserializer = new EventDeserializer();
        BinaryLogClient client = new BinaryLogClient("localhost", 3306, "root", "MySql2023!");
        client.setEventDeserializer(eventDeserializer);
        client.registerEventListener(new BinlogListener());


        try {
            client.connect();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {

        }
    }
}
