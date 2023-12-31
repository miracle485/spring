package com.example.mysqlbinlog.service;

import com.example.mysqlbinlog.config.DataSyncTasks;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class BinLogServiceStarter implements ApplicationListener<ApplicationReadyEvent> {
    private static final Logger LOGGER = LoggerFactory.getLogger(BinLogServiceStarter.class);

    @Resource
    private BinlogService binlogService;
    private static final ExecutorService EXECUTOR = Executors.newSingleThreadExecutor();

    @Resource
    private DataSyncTasks tasks;


    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        tasks.removeInvalidConfig();
        EXECUTOR.submit(binlogService::start);
    }

    public void shutDownClient() {
        try {
            binlogService.disConnect();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            EXECUTOR.shutdown();
            LOGGER.info("service executor has been shutdown");

        }
    }
}
