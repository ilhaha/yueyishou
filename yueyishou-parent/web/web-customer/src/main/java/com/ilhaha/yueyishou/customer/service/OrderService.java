package com.ilhaha.yueyishou.customer.service;

import com.ilhaha.yueyishou.common.result.Result;
import com.ilhaha.yueyishou.model.form.order.ServiceFeeRuleRequestForm;
import com.ilhaha.yueyishou.model.vo.order.CustomerOrderDetailsVo;
import com.ilhaha.yueyishou.model.vo.order.CustomerOrderListVo;
import com.ilhaha.yueyishou.model.vo.order.PlaceOrderForm;
import com.ilhaha.yueyishou.model.vo.order.ServiceFeeRuleResponseVo;

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
    Result<CustomerOrderDetailsVo> getOrderDetails(Long orderId);

    /**
     * 取消订单
     * @param orderId
     * @return
     */
    Result<Boolean> cancelOrder(Long orderId);
}
