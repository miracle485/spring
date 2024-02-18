package com.example.mysqlbinlog.service.handler;

import com.example.mysqlbinlog.service.handler.iface.EventHandler;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author = wangyilin29
 * @date = 2024/2/18
 **/
@Service
public class EventHandlerService {

    private List<EventHandler> eventHandlerList;
}
