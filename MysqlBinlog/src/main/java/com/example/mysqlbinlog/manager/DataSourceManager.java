package com.example.mysqlbinlog.manager;

import com.example.mysqlbinlog.config.DataSyncMysqlTaskConfig;
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


    public TargetWriteMapper getTargetMapper(DataSyncMysqlTaskConfig config) {
        if (config == null) {
            return null;
        }
        if (mapperMap.containsKey(config)) {
            return mapperMap.get(config);
        }
        synchronized (config) {
            return createTargetMapper(config);
        }
    }

    private TargetWriteMapper createTargetMapper(DataSyncMysqlTaskConfig config) {
        //添加配置信息
        DataSourceBuilder<?> dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.url(config.getTargetJdbcUrl());
        dataSourceBuilder.password(config.getTargetPassWord());
        dataSourceBuilder.username(config.getTargetUserName());


        DataSource build = dataSourceBuilder.build();

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
        registry.addMapper(TargetWriteMapper.class);
        //开启数据库session
        SqlSession sqlSession = sqlSessionFactory.openSession();

        //创建mapper的代理
        TargetWriteMapper mapper = registry.getMapper(TargetWriteMapper.class, sqlSession);

        mapperMap.put(config, mapper);
        sqlSessionMap.put(config, sqlSession);

        return mapper;
    }

    /**
     * 关闭所有的链接
     */
    public void shutDownAll(){
        for (SqlSession value : sqlSessionMap.values()) {
            value.close();
        }
    }
}
