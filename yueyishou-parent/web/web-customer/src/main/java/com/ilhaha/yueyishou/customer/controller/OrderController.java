package com.ilhaha.yueyishou.customer.controller;

import com.ilhaha.yueyishou.common.anno.LoginVerification;
import com.ilhaha.yueyishou.common.result.Result;
import com.ilhaha.yueyishou.customer.service.OrderService;
import com.ilhaha.yueyishou.model.form.order.*;
import com.ilhaha.yueyishou.model.vo.order.*;
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
     * 删除订单
     * @param orderDeleteForm
     * @return
     */
    @LoginVerification
    @PostMapping("/delete")
    public Result<Boolean> delete(@RequestBody OrderDeleteForm orderDeleteForm){
        return orderService.delete(orderDeleteForm);
    }

    /**
     * 回收员接单后，回收员、顾客取消订单
     * @param cancelOrderForm
     * @return
     */
    @LoginVerification
    @PostMapping("/cancelOrderAfterTaking")
    public Result<Boolean> cancelOrderAfterTaking(@RequestBody CancelOrderForm cancelOrderForm){
        return orderService.cancelOrderAfterTaking(cancelOrderForm);
    }

    /***
     * 结算取消订单费用
     * @param cancelOrderForm
     * @return
     */
    @LoginVerification
    @PostMapping("/calculateCancellationFee")
    public Result<CancelOrderFeeVo> calculateCancellationFee(@RequestBody CancelOrderFeeForm cancelOrderForm){
        return orderService.calculateCancellationFee(cancelOrderForm);
    }

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
