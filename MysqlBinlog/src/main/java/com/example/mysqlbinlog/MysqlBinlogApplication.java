package com.example.mysqlbinlog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableConfigurationProperties
public class MysqlBinlogApplication {

    public static void main(String[] args) {
        SpringApplication.run(MysqlBinlogApplication.class, args);
    }

}
