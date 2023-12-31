package com.example.mysqlbinlog.service;

import com.example.mysqlbinlog.manager.DataSourceManager;
import com.example.mysqlbinlog.manager.EsClientManager;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class ListenerShutdownService implements ApplicationListener<ContextClosedEvent> {
    @Resource
    private BinLogServiceStarter binLogServiceStarter;
    @Resource
    private EsClientManager esClientManager;
    @Resource
    private DataSourceManager dataSourceManager;
    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        binLogServiceStarter.shutDownClient();
        esClientManager.shutdownAll();
        dataSourceManager.shutDownAll();
    }
}
