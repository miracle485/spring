package com.example.mysqlbinlog;

import com.example.mysqlbinlog.config.DataSyncMysqlTaskConfig;
import com.example.mysqlbinlog.dao.target.TargetWriteMapper;
import com.example.mysqlbinlog.manager.DataSourceManager;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
public class MysqlTaskTest {
    @Resource
    private DataSourceManager manager;

    @Test
    void testSql() throws Exception {
        TargetWriteMapper mapper = getMapper();

        System.out.println(mapper.getTableListByDatabaseName("test"));
    }

    private TargetWriteMapper getMapper() throws Exception {
        DataSyncMysqlTaskConfig config=new DataSyncMysqlTaskConfig();
        config.setJdbcUrl("jdbc:mysql://localhost:3306/information_schema");
        config.setPassword("MySql2023!");
        config.setUserName("root");


        return manager.getTargetMapper(config);
    }

}
