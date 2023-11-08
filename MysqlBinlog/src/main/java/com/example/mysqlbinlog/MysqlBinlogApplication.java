package com.example.mysqlbinlog;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@MapperScan("com.example.mysqlbinlog.dao")
@EnableConfigurationProperties
public class MysqlBinlogApplication {

    public static void main(String[] args) {
        SpringApplication.run(MysqlBinlogApplication.class, args);
    }

}
