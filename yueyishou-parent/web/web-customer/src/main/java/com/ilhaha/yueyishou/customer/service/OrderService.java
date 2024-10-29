package com.ilhaha.yueyishou.customer.service;

import com.ilhaha.yueyishou.common.result.Result;
import com.ilhaha.yueyishou.model.form.order.*;
import com.ilhaha.yueyishou.model.vo.order.*;

import java.util.List;

/**
 * @Author ilhaha
 * @Create 2024/9/28 20:29
 * @Version 1.0
 */
public interface OrderService {

    /**
     * 预估订单费用
     * @param calculateOrderFeeForm
     * @return
     */
    Result<ServiceFeeRuleResponseVo> calculateOrderFee(ServiceFeeRuleRequestForm calculateOrderFeeForm);

    /**
     * 下单
     * @param placeOrderForm
     * @return
     */
    Result<Boolean> placeOrder(PlaceOrderForm placeOrderForm);

    /**
     * 根据订单状态获取订单列表
     * @return
     */
    Result<List<CustomerOrderListVo>> orderList(Integer status);

    /**
     * 根据订单ID获取订单详情
     */
    Result<OrderDetailsVo> getOrderDetails(Long orderId);

    /**
     * 取消订单
     * @param orderId
     * @return
     */
    Result<Boolean> cancelOrder(Long orderId);

    /**
     * 顾客评论
     * @return
     */
    Result<Boolean> review(ReviewForm reviewForm);

    /***
     * 结算取消订单费用
     * @param cancelOrderForm
     * @return
     */
    Result<CancelOrderFeeVo> calculateCancellationFee(CancelOrderFeeForm cancelOrderForm);

    /**
     * 回收员接单后，回收员、顾客取消订单
     * @param cancelOrderForm
     * @return
     */
    Result<Boolean> cancelOrderAfterTaking(CancelOrderForm cancelOrderForm);

    /**
     * 删除订单
     * @param orderDeleteForm
     * @return
     */
    Result<Boolean> delete(OrderDeleteForm orderDeleteForm);
}
