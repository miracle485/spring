package com.example.mysqlbinlog;

import com.example.mysqlbinlog.config.SourceMySqlConfig;
import com.example.mysqlbinlog.dao.source.SourceDataMapper;
import com.example.mysqlbinlog.dao.target.TargetWriteMapper;
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

import javax.sql.DataSource;

@SpringBootTest
public class MysqlTaskTest {
    @Test
    void testSql() throws Exception {
        SourceDataMapper mapper = getMapper(SourceDataMapper.class);

        System.out.println(mapper.getTableInfoByName("testtable"));
    }

    private <T> T getMapper(Class<T> type) throws Exception {
        DataSourceBuilder<?> dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.url("jdbc:mysql://localhost:3306/information_schema");
        dataSourceBuilder.password("MySql2023!");
        dataSourceBuilder.username("root");


        DataSource build = dataSourceBuilder.build();

        SqlSessionFactoryBean sqlSessionFactoryBean=new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(build);
        SqlSessionFactory sqlSessionFactory = sqlSessionFactoryBean.getObject();
        Configuration configuration = sqlSessionFactory.getConfiguration();
        System.out.println(configuration.getMappedStatements());
        MapperRegistry registry=new MapperRegistry(configuration);
        registry.addMapper(SourceDataMapper.class);


        SqlSession sqlSession = sqlSessionFactory.openSession();

        T mapper = registry.getMapper(type, sqlSession);
        return mapper;
    }

}
