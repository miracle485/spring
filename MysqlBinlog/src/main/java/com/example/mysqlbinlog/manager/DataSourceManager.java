package com.example.mysqlbinlog.manager;

import com.example.mysqlbinlog.config.DataSyncMysqlTaskConfig;
import com.example.mysqlbinlog.config.SourceMySqlConfig;
import com.example.mysqlbinlog.config.iface.DataSourceConfigIface;
import com.example.mysqlbinlog.dao.source.SourceDataMapper;
import com.example.mysqlbinlog.dao.target.TargetWriteMapper;
import com.google.common.collect.Maps;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.binding.MapperRegistry;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class DataSourceManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(DataSourceManager.class);

    private final Map<DataSyncMysqlTaskConfig, TargetWriteMapper> mapperMap = Maps.newConcurrentMap();
    private final Map<DataSourceConfigIface, SqlSession> sqlSessionMap = Maps.newConcurrentMap();
    private final Map<DataSourceConfigIface, HikariDataSource> dataSourceMap = Maps.newConcurrentMap();

    private volatile SourceDataMapper sourceDataMapper;


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

            sourceDataMapper = createTargetMapper(sourceMySqlConfig, SourceDataMapper.class);

            return sourceDataMapper;
        }
    }


    private <T> T createTargetMapper(DataSourceConfigIface config, Class<T> tClass) {
        HikariDataSource dataSource = createDataSource(config);

        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
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
        dataSourceMap.put(config, dataSource);

        return mapper;
    }

    private HikariDataSource createDataSource(DataSourceConfigIface config) {
        //添加配置信息
        HikariDataSource hikariDataSource = new HikariDataSource();
        hikariDataSource.setUsername(config.getUserName());
        hikariDataSource.setJdbcUrl(config.getJdbcUrl());
        hikariDataSource.setPassword(config.getPassword());

        return hikariDataSource;
    }

    /**
     * 关闭所有的链接
     */
    public void shutDownAll() {
        for (SqlSession value : sqlSessionMap.values()) {
            value.close();
        }
        for (HikariDataSource value : dataSourceMap.values()) {
            value.close();
        }
    }
}
