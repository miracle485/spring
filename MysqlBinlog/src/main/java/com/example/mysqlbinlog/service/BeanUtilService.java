package com.example.mysqlbinlog.service;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

@Service
public class BeanUtilService implements ApplicationContextAware {
    private static ApplicationContext APPLICATION_CONTEXT;

    private BeanUtilService() {
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        APPLICATION_CONTEXT = applicationContext;
    }

    public static  <T> T getBean(Class<?> classz){
        return (T) APPLICATION_CONTEXT.getBean(classz);
    }
}
