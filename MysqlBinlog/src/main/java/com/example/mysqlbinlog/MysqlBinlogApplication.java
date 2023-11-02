package com.example.mysqlbinlog;

import com.example.mysqlbinlog.service.BinlogService;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.mysqlbinlog.dao")
public class MysqlBinlogApplication {

    public static void main(String[] args) {
        SpringApplication.run(MysqlBinlogApplication.class, args);
        BinlogService binlogService = new BinlogService();
        binlogService.startListener();
    }

}
