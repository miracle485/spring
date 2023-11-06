package com.example.mysqlbinlog.service;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class BeanUtilService implements ApplicationContextAware {
    private static ApplicationContext APPLICATION_CONTEXT;

    private BeanUtilService() {
    }


    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        APPLICATION_CONTEXT = applicationContext;
    }

    public static <T> T getBean(Class<T> classz) {
        return APPLICATION_CONTEXT.getBean(classz);
    }

    public static <T> Map<String, T> getBeanList(Class<T> classz) {
        return  APPLICATION_CONTEXT.getBeansOfType(classz);
    }
}
