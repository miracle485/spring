package com.example.mysqlbinlog.config.factory;

import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertySourceFactory;

import java.io.IOException;
import java.util.Properties;

public class YamlFactory implements PropertySourceFactory {
    @Override
    public PropertySource<?> createPropertySource(String name, EncodedResource resource) throws IOException {
        Properties properties = loadYamlConfig(resource);
        name = name == null ? resource.getResource().getFilename() : name;


        return new PropertiesPropertySource(name, properties);
    }

    private Properties loadYamlConfig(EncodedResource resource) {
        YamlPropertiesFactoryBean factoryBean = new YamlPropertiesFactoryBean();
        factoryBean.setResources(resource.getResource());
        factoryBean.afterPropertiesSet();

        return factoryBean.getObject();
    }
}
