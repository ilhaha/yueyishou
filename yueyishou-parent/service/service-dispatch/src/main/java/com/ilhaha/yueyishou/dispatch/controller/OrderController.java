package com.ilhaha.yueyishou.dispatch.controller;

import com.ilhaha.yueyishou.common.result.Result;
import com.ilhaha.yueyishou.dispatch.service.OrderService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author ilhaha
 * @Create 2024/10/13 11:25
 * @Version 1.0
 */
@RestController
@RequestMapping("/dispatch/order")
public class OrderController {

    @Resource
    private OrderService orderService;

    /**
     * 创建定时取消到预约时间还没接单的订单任务
     * @return
     */
    @PostMapping("/processTimeoutOrders")
    public Result<Long> processTimeoutOrdersTask(){
        return Result.ok(orderService.processTimeoutOrdersTask());
    }

}
