package com.example.mysqlbinlog.service;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class BinLogServiceStarter implements ApplicationListener<ApplicationReadyEvent> {
    @Resource
    private BinlogService binlogService;
    private static final ExecutorService EXECUTOR = Executors.newSingleThreadExecutor();


    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        EXECUTOR.submit(binlogService::start);
    }

    public void shutDownClient() {
        try {
            binlogService.disConnect();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }finally {
            EXECUTOR.shutdown();
        }
    }
}
