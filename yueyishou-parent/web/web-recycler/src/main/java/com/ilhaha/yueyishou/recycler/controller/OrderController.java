package com.ilhaha.yueyishou.recycler.controller;

import com.ilhaha.yueyishou.common.anno.LoginVerification;
import com.ilhaha.yueyishou.common.result.Result;
import com.ilhaha.yueyishou.model.form.order.MatchingOrderForm;
import com.ilhaha.yueyishou.model.vo.order.OrderDetailsVo;
import com.ilhaha.yueyishou.model.vo.order.MatchingOrderVo;
import com.ilhaha.yueyishou.model.vo.order.RecyclerOrderVo;
import com.ilhaha.yueyishou.recycler.service.OrderService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author ilhaha
 * @Create 2024/10/12 16:41
 * @Version 1.0
 */
@RestController
@RequestMapping("/order")
public class OrderController {

    @Resource
    private OrderService orderService;

    /**
     * 根据状态获取回收员订单列表
     * @param status
     * @return
     */
    @LoginVerification
    @GetMapping("/list/{status}")
    public Result<List<RecyclerOrderVo>> getRecyclerOrderListByStatus(@PathVariable("status") Integer status){
        return orderService.getRecyclerOrderListByStatus(status);
    }

    /**
     * 回收员抢单
     * @param orderId
     * @return
     */
    @LoginVerification
    @PostMapping("/grab/{orderId}")
    public Result<Boolean> grabOrder(@PathVariable("orderId") Long orderId){
        return orderService.grabOrder(orderId);
    }

    /**
     * 回收员根据订单ID获取订单详情
     */
    @GetMapping("/details/{orderId}")
    @LoginVerification
    public Result<OrderDetailsVo> getOrderDetails(@PathVariable("orderId") Long orderId) {
        return orderService.getOrderDetails(orderId);
    }

    /**
     * 回收员获取符合接单的订单
     * @param matchingOrderForm
     * @return
     */
    @LoginVerification
    @PostMapping("/matching")
    public Result<List<MatchingOrderVo>> retrieveMatchingOrders(@RequestBody MatchingOrderForm matchingOrderForm){
        return orderService.retrieveMatchingOrders(matchingOrderForm);
    }
}
