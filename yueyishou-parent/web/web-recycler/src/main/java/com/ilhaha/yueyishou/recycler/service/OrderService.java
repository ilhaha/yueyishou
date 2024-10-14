package com.ilhaha.yueyishou.recycler.service;

import com.ilhaha.yueyishou.common.result.Result;
import com.ilhaha.yueyishou.model.form.order.MatchingOrderForm;
import com.ilhaha.yueyishou.model.vo.order.OrderDetailsVo;
import com.ilhaha.yueyishou.model.vo.order.MatchingOrderVo;
import com.ilhaha.yueyishou.model.vo.order.RecyclerOrderVo;

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
}
