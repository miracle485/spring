package com.example.mysqlbinlog.manager;

import com.example.mysqlbinlog.config.DataSyncMysqlTaskConfig;
import com.example.mysqlbinlog.config.SourceMySqlConfig;
import com.example.mysqlbinlog.dao.source.SourceDataMapper;
import com.example.mysqlbinlog.dao.target.TargetWriteMapper;
import com.google.common.collect.Maps;
import org.apache.ibatis.binding.MapperRegistry;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.Map;

@Service
public class DataSourceManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(DataSourceManager.class);

    private final Map<DataSyncMysqlTaskConfig, TargetWriteMapper> mapperMap = Maps.newConcurrentMap();
    private final Map<DataSyncMysqlTaskConfig, SqlSession> sqlSessionMap = Maps.newConcurrentMap();

    private volatile SourceDataMapper sourceDataMapper;
    private SqlSession sourceSqlSession;


    /**
     * 创建目标表的mapper，通常情况下这个mapper应该是与配置一一对应的
     * 一般情况下，我们认为这里的目标表是mysql类型的
     */
    public TargetWriteMapper getTargetMapper(DataSyncMysqlTaskConfig config) {
        if (config == null) {
            return null;
        }
        if (mapperMap.containsKey(config)) {
            return mapperMap.get(config);
        }
        synchronized (config) {
            if (mapperMap.containsKey(config)) {
                return mapperMap.get(config);
            }
            TargetWriteMapper targetMapper = createTargetMapper(config, TargetWriteMapper.class);
            mapperMap.put(config, targetMapper);

            return targetMapper;
        }
    }

    /**
     * 创建数据源的mapper
     */
    public SourceDataMapper getSourceMapper(SourceMySqlConfig sourceMySqlConfig) {
        if (sourceMySqlConfig == null) {
            return null;
        }
        if (sourceDataMapper != null) {
            return sourceDataMapper;
        }

        synchronized (sourceMySqlConfig) {
            if (sourceDataMapper != null) {
                return sourceDataMapper;
            }
            DataSourceBuilder<?> dataSourceBuilder = DataSourceBuilder.create();
            dataSourceBuilder.url(sourceMySqlConfig.getJdbcUrl());
            dataSourceBuilder.password(sourceMySqlConfig.getPassword());
            dataSourceBuilder.username(sourceMySqlConfig.getUserName());

            SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
            sqlSessionFactoryBean.setDataSource(dataSourceBuilder.build());
            SqlSessionFactory sqlSessionFactory;
            try {
                sqlSessionFactory = sqlSessionFactoryBean.getObject();
            } catch (Exception e) {
                LOGGER.error("error when create SqlSessionFactory mapper will be null,error is ", e);
                return null;
            }
            Configuration configuration = sqlSessionFactory.getConfiguration();
            //注册mapper信息
            MapperRegistry registry = new MapperRegistry(configuration);
            registry.addMapper(SourceDataMapper.class);
            //开启数据库session
            sourceSqlSession = sqlSessionFactory.openSession();

            //创建mapper的代理
            sourceDataMapper = registry.getMapper(SourceDataMapper.class, sourceSqlSession);

            return sourceDataMapper;
        }
    }


    private <T> T createTargetMapper(DataSyncMysqlTaskConfig config, Class<T> tClass) {
        DataSource build = createDataSource(config);

        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(build);
        SqlSessionFactory sqlSessionFactory;
        try {
            sqlSessionFactory = sqlSessionFactoryBean.getObject();
        } catch (Exception e) {
            LOGGER.error("error when create SqlSessionFactory mapper will be null,error is ", e);
            return null;
        }
        Configuration configuration = sqlSessionFactory.getConfiguration();
        //注册mapper信息
        MapperRegistry registry = new MapperRegistry(configuration);
        registry.addMapper(tClass);
        //开启数据库session
        SqlSession sqlSession = sqlSessionFactory.openSession();

        //创建mapper的代理
        T mapper = registry.getMapper(tClass, sqlSession);

        sqlSessionMap.put(config, sqlSession);

        return mapper;
    }

    private DataSource createDataSource(DataSyncMysqlTaskConfig config) {
        //添加配置信息
        DataSourceBuilder<?> dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.url(config.getTargetJdbcUrl());
        dataSourceBuilder.password(config.getTargetPassWord());
        dataSourceBuilder.username(config.getTargetUserName());

        return dataSourceBuilder.build();
    }

    /**
     * 关闭所有的链接
     */
    public void shutDownAll() {
        for (SqlSession value : sqlSessionMap.values()) {
            value.close();
        }

        sourceSqlSession.close();
    }
}
