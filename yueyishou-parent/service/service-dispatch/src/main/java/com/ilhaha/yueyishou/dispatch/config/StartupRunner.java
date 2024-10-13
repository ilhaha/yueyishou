package com.ilhaha.yueyishou.dispatch.config;

import com.ilhaha.yueyishou.dispatch.service.OrderService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class StartupRunner implements ApplicationRunner {

    @Resource
    private OrderService orderService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        orderService.processTimeoutOrdersTask();
    }
}