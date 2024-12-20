package com.ilhaha.yueyishou.recycler.service.impl;

import com.ilhaha.yueyishou.common.result.Result;
import com.ilhaha.yueyishou.common.util.AuthContextHolder;
import com.ilhaha.yueyishou.model.form.order.*;
import com.ilhaha.yueyishou.model.vo.order.*;
import com.ilhaha.yueyishou.order.client.OrderInfoFeignClient;
import com.ilhaha.yueyishou.order.client.OrderRejectionOperateFeignClient;
import com.ilhaha.yueyishou.recycler.service.OrderService;
import com.ilhaha.yueyishou.rules.client.ServiceFeeRuleFeignClient;
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

    @Resource
    private OrderRejectionOperateFeignClient orderRejectionOperateFeignClient;


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

    /**
     * 计算实际的订单信息
     * @param orderId
     * @return
     */
    @Override
    public Result<CalculateActualOrderVo> calculateActual(Long orderId) {
        return orderInfoFeignClient.calculateActual(orderId);
    }

    /**
     * 回收员校验回收码
     * @param validateRecycleCodeForm
     * @return
     */
    @Override
    public Result<Boolean> validateRecycleCode(ValidateRecycleCodeForm validateRecycleCodeForm) {
        return orderInfoFeignClient.validateRecycleCode(validateRecycleCodeForm);
    }

    /**
     * 结算订单
     * @param settlementForm
     * @return
     */
    @Override
    public Result<Boolean> settlement(SettlementForm settlementForm) {
        settlementForm.setRecyclerId(AuthContextHolder.getRecyclerId());
        return orderInfoFeignClient.settlement(settlementForm);
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
        orderDeleteForm.setRecyclerId(AuthContextHolder.getRecyclerId());
        return orderInfoFeignClient.delete(orderDeleteForm);
    }

    /**
     * 回收员拒收订单
     * @param rejectOrderForm
     * @return
     */
    @Override
    public Result<Boolean> reject(RejectOrderForm rejectOrderForm) {
        return orderInfoFeignClient.reject(rejectOrderForm);
    }

    /**
     * 获取拒收订单信息
     * @param orderId
     * @return
     */
    @Override
    public Result<RejectOrderVo> getRejectInfo(Long orderId) {
        return orderInfoFeignClient.getRejectInfo(orderId);
    }

    /**
     * 取消申请订单拒收
     * @param orderId
     * @return
     */
    @Override
    public Result<Boolean> cancelReject(Long orderId) {
        return orderInfoFeignClient.cancelReject(orderId);
    }

    /**
     * 获取回收员申请拒收订单被驳回反馈信息
     * @param orderId
     * @return
     */
    @Override
    public Result<RejectInfoVo> rejectFeedback(Long orderId) {
        return orderRejectionOperateFeignClient.rejectFeedback(orderId);
    }
}
