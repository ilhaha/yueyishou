package com.ilhaha.yueyishou.recycler.service;

import com.ilhaha.yueyishou.common.result.Result;
import com.ilhaha.yueyishou.model.form.order.*;
import com.ilhaha.yueyishou.model.vo.order.*;

import java.util.List;

/**
 * @Author ilhaha
 * @Create 2024/10/12 16:41
 * @Version 1.0
 */
public interface OrderService {

    /**
     * 回收员获取符合接单的订单
     * @param matchingOrderForm
     * @return
     */
    Result<List<MatchingOrderVo>> retrieveMatchingOrders(MatchingOrderForm matchingOrderForm);

    /**
     * 回收员根据订单ID获取订单详情
     */
    Result<OrderDetailsVo> getOrderDetails(Long orderId);

    /**
     * 回收员抢单
     * @param orderId
     * @return
     */
    Result<Boolean> grabOrder(Long orderId);

    /**
     * 根据状态获取回收员订单列表
     * @param status
     * @return
     */
    Result<List<RecyclerOrderVo>> getRecyclerOrderListByStatus(Integer status);

    /**
     * 回收员接单后，在预约时间前一个小时取消订单时，要重新把订单给别的回收员接单
     * @param orderId
     * @return
     */
    Result<Boolean> repost(Long orderId);

    /**
     * 回收员到达回收点
     * @param orderId
     * @return
     */
    Result<Boolean> arrive(Long orderId);

    /**
     * 预估订单费用
     * @param calculateOrderFeeForm
     * @return
     */
    Result<ServiceFeeRuleResponseVo> calculateOrderFee(ServiceFeeRuleRequestForm calculateOrderFeeForm);

    /**
     * 更新订单信息
     * @param updateOrderFrom
     * @return
     */
    Result<Boolean> updateOrder(UpdateOrderFrom updateOrderFrom);

    /**
     * 计算实际的订单信息
     * @param orderId
     * @return
     */
    Result<CalculateActualOrderVo> calculateActual(Long orderId);

    /**
     * 回收员校验回收码
     * @param validateRecycleCodeForm
     * @return
     */
    Result<Boolean> validateRecycleCode(ValidateRecycleCodeForm validateRecycleCodeForm);

    /**
     * 结算订单
     * @param settlementForm
     * @return
     */
    Result<Boolean> settlement(SettlementForm settlementForm);
}
