package com.example.mysqlbinlog.controller;

import com.example.mysqlbinlog.config.DataSyncTasks;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("tasks")
public class ConfigController {
    @Resource
    private DataSyncTasks tasks;
    @GetMapping("info")
    public DataSyncTasks getHello() {
        return tasks;
    }
}
