package com.example.mysqlbinlog;

import com.mysql.cj.xdevapi.SqlStatement;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionManager;
import org.apache.ibatis.session.defaults.DefaultSqlSessionFactory;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;

@SpringBootTest
public class MysqlTaskTest {
    @Test
    void testSql() throws Exception {
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

        SqlSession sqlSession = sqlSessionFactory.openSession();
        SqlSessionManager sqlSessionManager=new SqlSessionMpper




        sqlSession.close();


    }
}
