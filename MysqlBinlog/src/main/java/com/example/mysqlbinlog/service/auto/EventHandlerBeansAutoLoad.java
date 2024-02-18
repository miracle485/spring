package com.example.mysqlbinlog.service.auto;

import com.example.mysqlbinlog.service.BeanUtilService;
import com.example.mysqlbinlog.service.handler.iface.EventHandler;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author = wangyilin29
 * @date = 2024/2/18
 **/
@Configuration
public class EventHandlerBeansAutoLoad {
    @Bean
    public List<EventHandler> eventHandlerList(){
        List<EventHandler> beanList = BeanUtilService.getBeanList(EventHandler.class);
        return beanList;
    }
}
