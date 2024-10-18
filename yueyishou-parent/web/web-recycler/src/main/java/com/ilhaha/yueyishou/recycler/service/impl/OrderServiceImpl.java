package com.ilhaha.yueyishou.recycler.service.impl;

import com.ilhaha.yueyishou.common.result.Result;
import com.ilhaha.yueyishou.common.util.AuthContextHolder;
import com.ilhaha.yueyishou.model.form.order.MatchingOrderForm;
import com.ilhaha.yueyishou.model.form.order.ServiceFeeRuleRequestForm;
import com.ilhaha.yueyishou.model.form.order.UpdateOrderFrom;
import com.ilhaha.yueyishou.model.vo.order.OrderDetailsVo;
import com.ilhaha.yueyishou.model.vo.order.MatchingOrderVo;
import com.ilhaha.yueyishou.model.vo.order.RecyclerOrderVo;
import com.ilhaha.yueyishou.model.vo.order.ServiceFeeRuleResponseVo;
import com.ilhaha.yueyishou.order.client.OrderInfoFeignClient;
import com.ilhaha.yueyishou.recycler.service.OrderService;
import com.ilhaha.yueyishou.reles.client.ServiceFeeRuleFeignClient;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author ilhaha
 * @Create 2024/10/12 16:41
 * @Version 1.0
 */
@Service
public class OrderServiceImpl implements OrderService {

    @Resource
    private OrderInfoFeignClient orderInfoFeignClient;

    @Resource
    private ServiceFeeRuleFeignClient serviceFeeRuleFeignClient;


    /**
     * 回收员获取符合接单的订单
     * @param matchingOrderForm
     * @return
     */
    @Override
    public Result<List<MatchingOrderVo>> retrieveMatchingOrders(MatchingOrderForm matchingOrderForm) {
        matchingOrderForm.setCustomerId(AuthContextHolder.getCustomerId());
        matchingOrderForm.setRecyclerId(AuthContextHolder.getRecyclerId());
        return orderInfoFeignClient.retrieveMatchingOrders(matchingOrderForm);
    }

    /**
     * 回收员根据订单ID获取订单详情
     */
    @Override
    public Result<OrderDetailsVo> getOrderDetails(Long orderId) {
        return orderInfoFeignClient.getOrderDetails(AuthContextHolder.getRecyclerId(),orderId);
    }

    /**
     * 回收员抢单
     * @param orderId
     * @return
     */
    @Override
    public Result<Boolean> grabOrder(Long orderId) {
        return orderInfoFeignClient.grabOrder(AuthContextHolder.getRecyclerId(),orderId);
    }

    /**
     * 根据状态获取回收员订单列表
     * @param status
     * @return
     */
    @Override
    public Result<List<RecyclerOrderVo>> getRecyclerOrderListByStatus(Integer status) {
        return orderInfoFeignClient.getRecyclerOrderListByStatus(AuthContextHolder.getRecyclerId(),status);
    }

    /**
     * 回收员接单后，在预约时间前一个小时取消订单时，要重新把订单给别的回收员接单
     * @param orderId
     * @return
     */
    @Override
    public Result<Boolean> repost(Long orderId) {
        return orderInfoFeignClient.repost(orderId);
    }

    /**
     * 回收员到达回收点
     * @param orderId
     * @return
     */
    @Override
    public Result<Boolean> arrive(Long orderId) {
        return orderInfoFeignClient.arrive(orderId);
    }

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
     * 更新订单信息
     * @param updateOrderFrom
     * @return
     */
    @Override
    public Result<Boolean> updateOrder(UpdateOrderFrom updateOrderFrom) {
        return orderInfoFeignClient.updateOrder(updateOrderFrom);
    }
}
