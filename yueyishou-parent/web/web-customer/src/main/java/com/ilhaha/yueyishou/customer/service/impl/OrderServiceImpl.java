package com.ilhaha.yueyishou.customer.service.impl;

import com.ilhaha.yueyishou.common.result.Result;
import com.ilhaha.yueyishou.common.util.AuthContextHolder;
import com.ilhaha.yueyishou.customer.service.OrderService;
import com.ilhaha.yueyishou.model.form.order.ServiceFeeRuleRequestForm;
import com.ilhaha.yueyishou.model.vo.order.OrderDetailsVo;
import com.ilhaha.yueyishou.model.vo.order.CustomerOrderListVo;
import com.ilhaha.yueyishou.model.vo.order.PlaceOrderForm;
import com.ilhaha.yueyishou.model.vo.order.ServiceFeeRuleResponseVo;
import com.ilhaha.yueyishou.order.client.OrderInfoFeignClient;
import com.ilhaha.yueyishou.reles.client.ServiceFeeRuleFeignClient;
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
}
