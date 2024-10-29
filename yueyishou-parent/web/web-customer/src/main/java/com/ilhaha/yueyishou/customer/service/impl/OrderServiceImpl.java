package com.ilhaha.yueyishou.customer.service.impl;

import com.ilhaha.yueyishou.common.result.Result;
import com.ilhaha.yueyishou.common.util.AuthContextHolder;
import com.ilhaha.yueyishou.customer.service.OrderService;
import com.ilhaha.yueyishou.model.form.order.*;
import com.ilhaha.yueyishou.model.vo.order.*;
import com.ilhaha.yueyishou.order.client.OrderCommentFeignClient;
import com.ilhaha.yueyishou.order.client.OrderInfoFeignClient;
import com.ilhaha.yueyishou.rules.client.ServiceFeeRuleFeignClient;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author ilhaha
 * @Create 2024/9/28 20:29
 * @Version 1.0
 */
@Service
public class OrderServiceImpl implements OrderService {

    @Resource
    private ServiceFeeRuleFeignClient serviceFeeRuleFeignClient;

    @Resource
    private OrderInfoFeignClient orderInfoFeignClient;

    @Resource
    private OrderCommentFeignClient orderCommentFeignClient;

    /**
     * 预估订单费用
     * @param calculateOrderFeeForm
     * @return
     */
    @Override
    public Result<ServiceFeeRuleResponseVo> calculateOrderFee(ServiceFeeRuleRequestForm calculateOrderFeeForm) {
        return serviceFeeRuleFeignClient.calculateOrderFee(calculateOrderFeeForm);
    }

    /**
     * 下单
     * @param placeOrderForm
     * @return
     */
    @Override
    public Result<Boolean> placeOrder(PlaceOrderForm placeOrderForm) {
        placeOrderForm.setCustomerId(AuthContextHolder.getCustomerId());
        return orderInfoFeignClient.placeOrder(placeOrderForm);
    }

    /**
     * 根据订单状态获取订单列表
     * @return
     */
    @Override
    public Result<List<CustomerOrderListVo>> orderList(Integer status) {
        return orderInfoFeignClient.orderList(status,AuthContextHolder.getCustomerId());
    }

    /**
     * 根据订单ID获取订单详情
     */
    @Override
    public Result<OrderDetailsVo> getOrderDetails(Long orderId) {
        return orderInfoFeignClient.getOrderDetails(orderId);
    }

    /**
     * 取消订单
     * @param orderId
     * @return
     */
    @Override
    public Result<Boolean> cancelOrder(Long orderId) {
        return orderInfoFeignClient.cancelOrder(orderId);
    }

    /**
     * 顾客评论
     * @return
     */
    @Override
    public Result<Boolean> review(ReviewForm reviewForm) {
        return orderCommentFeignClient.review(reviewForm);
    }

    /***
     * 结算取消订单费用
     * @param cancelOrderForm
     * @return
     */
    @Override
    public Result<CancelOrderFeeVo> calculateCancellationFee(CancelOrderFeeForm cancelOrderForm) {
        return orderInfoFeignClient.calculateCancellationFee(cancelOrderForm);
    }

    /**
     * 回收员接单后，回收员、顾客取消订单
     * @param cancelOrderForm
     * @return
     */
    @Override
    public Result<Boolean> cancelOrderAfterTaking(CancelOrderForm cancelOrderForm) {
        return orderInfoFeignClient.cancelOrderAfterTaking(cancelOrderForm);
    }

    /**
     * 删除订单
     * @param orderDeleteForm
     * @return
     */
    @Override
    public Result<Boolean> delete(OrderDeleteForm orderDeleteForm) {
        orderDeleteForm.setCustomerId(AuthContextHolder.getCustomerId());
        return orderInfoFeignClient.delete(orderDeleteForm);
    }
}
