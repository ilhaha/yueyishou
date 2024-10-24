package com.ilhaha.yueyishou.customer.controller;

import com.ilhaha.yueyishou.common.anno.LoginVerification;
import com.ilhaha.yueyishou.common.result.Result;
import com.ilhaha.yueyishou.customer.service.OrderService;
import com.ilhaha.yueyishou.model.form.order.ReviewForm;
import com.ilhaha.yueyishou.model.form.order.ServiceFeeRuleRequestForm;
import com.ilhaha.yueyishou.model.vo.order.OrderDetailsVo;
import com.ilhaha.yueyishou.model.vo.order.CustomerOrderListVo;
import com.ilhaha.yueyishou.model.vo.order.PlaceOrderForm;
import com.ilhaha.yueyishou.model.vo.order.ServiceFeeRuleResponseVo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author ilhaha
 * @Create 2024/9/28 20:28
 * @Version 1.0
 */
@RestController
@RequestMapping("/order")
public class OrderController {

    @Resource
    private OrderService orderService;

    /**
     * 顾客评论
     * @return
     */
    @LoginVerification
    @PostMapping("/review")
    public Result<Boolean> review(@RequestBody ReviewForm reviewForm){
        return orderService.review(reviewForm);
    }

    /**
     * 取消订单
     * @param orderId
     * @return
     */
    @LoginVerification
    @PostMapping("/cancel/{orderId}")
    public Result<Boolean> cancelOrder(@PathVariable("orderId") Long orderId){
        return orderService.cancelOrder(orderId);
    }


    /**
     * 根据订单ID获取订单详情
     */
    @GetMapping("/details/{orderId}")
    @LoginVerification
    public Result<OrderDetailsVo> getOrderDetails(@PathVariable("orderId") Long orderId) {
        return orderService.getOrderDetails(orderId);
    }

    /**
     * 根据订单状态获取订单列表
     * @return
     */
    @LoginVerification
    @GetMapping("/list/{status}")
    public Result<List<CustomerOrderListVo>> orderList(@PathVariable("status") Integer status){
        return orderService.orderList(status);
    }

    /**
     * 下单
     * @param placeOrderForm
     * @return
     */
    @LoginVerification
    @PostMapping("/place")
    public Result<Boolean> placeOrder(@RequestBody PlaceOrderForm placeOrderForm) {
        return orderService.placeOrder(placeOrderForm);
    }

    /**
     * 预估订单费用
     * @param calculateOrderFeeForm
     * @return
     */
    @LoginVerification
    @PostMapping("/calculateOrderFee")
    public Result<ServiceFeeRuleResponseVo> calculateOrderFee(@RequestBody ServiceFeeRuleRequestForm calculateOrderFeeForm) {
        return orderService.calculateOrderFee(calculateOrderFeeForm);
    }
}
