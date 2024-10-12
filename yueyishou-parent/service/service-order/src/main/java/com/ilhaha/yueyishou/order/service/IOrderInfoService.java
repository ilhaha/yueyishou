package com.ilhaha.yueyishou.order.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ilhaha.yueyishou.model.entity.order.OrderInfo;
import com.ilhaha.yueyishou.model.form.order.MatchingOrderForm;
import com.ilhaha.yueyishou.model.form.order.OrderMgrQueryForm;
import com.ilhaha.yueyishou.model.vo.order.*;

import java.util.List;

public interface IOrderInfoService extends IService<OrderInfo> {

    /**
     * 订单分页列表查询
     *
     * @param orderMgrQueryForm
     * @param page
     * @return
     */
    Page<OrderMgrQueryVo> queryPageList(OrderMgrQueryForm orderMgrQueryForm, Page<OrderMgrQueryVo> page);

    /**
     * 下单
     * @param placeOrderForm
     * @return
     */
    Boolean placeOrder(PlaceOrderForm placeOrderForm);

    /**
     * 根据订单状态获取订单列表
     * @param status
     * @return
     */
    List<CustomerOrderListVo> orderList(Integer status, Long customerId);

    /**
     * 根据订单ID获取订单详情
     */
    OrderDetailsVo getOrderDetails(Long orderId);

    /**
     * 取消订单
     * @param orderId
     * @return
     */
    Boolean cancelOrder(Long orderId);

    /**
     * 回收员获取符合接单的订单
     * @param matchingOrderForm
     * @return
     */
    List<MatchingOrderVo> retrieveMatchingOrders(MatchingOrderForm matchingOrderForm);

    /**
     * 回收员抢单
     * @param recyclerId
     * @param orderId
     * @return
     */
    Boolean grabOrder(Long recyclerId, Long orderId);
}
