package com.example.mysqlbinlog;

import com.example.mysqlbinlog.config.DataSyncMysqlTaskConfig;
import com.example.mysqlbinlog.config.SourceMySqlConfig;
import com.example.mysqlbinlog.dao.source.SourceDataMapper;
import com.example.mysqlbinlog.dao.target.TargetWriteMapper;
import com.example.mysqlbinlog.manager.DataSourceManager;
import com.mysql.cj.xdevapi.SqlStatement;
import org.apache.ibatis.binding.MapperRegistry;
import org.apache.ibatis.builder.StaticSqlSource;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionManager;
import org.apache.ibatis.session.defaults.DefaultSqlSessionFactory;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionUtils;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import javax.sql.DataSource;

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
        config.setTargetJdbcUrl("jdbc:mysql://localhost:3306/information_schema");
        config.setTargetPassWord("MySql2023!");
        config.setTargetUserName("root");


        return manager.getTargetMapper(config);
    }

}
