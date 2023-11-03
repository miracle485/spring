package com.example.mysqlbinlog.service;

import com.example.mysqlbinlog.dao.InformationMapper;
import com.example.mysqlbinlog.mode.TableColumInfo;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

@Component
public class BinLogServiceStarter implements ApplicationListener<ApplicationReadyEvent> {
    @Resource
    private BinlogService binlogService;


    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        binlogService.start();
    }

    public void shutDownClient(){
        try {
            binlogService.disConnect();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
