package com.example.mysqlbinlog.service;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class ListenerShutdownService implements ApplicationListener<ContextClosedEvent> {
    @Resource
    private BinLogServiceStarter binLogServiceStarter;
    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        binLogServiceStarter.shutDownClient();
    }
}
