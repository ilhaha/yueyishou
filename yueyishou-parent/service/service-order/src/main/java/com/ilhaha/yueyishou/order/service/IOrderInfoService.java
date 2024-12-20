package com.ilhaha.yueyishou.order.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ilhaha.yueyishou.model.entity.order.OrderInfo;
import com.ilhaha.yueyishou.model.form.order.*;
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
     * 顾客根据订单ID获取订单详情
     * @param orderId
     * @return
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


    /**
     * 回收员根据订单ID获取订单详情
     * @param recyclerId
     * @param orderId
     * @return
     */
    OrderDetailsVo getOrderDetails(Long recyclerId, Long orderId);

    /**
     * 取消预约时间超时的订单
     * @return
     */
    Boolean processTimeoutOrders(List<Long> timeoutOrderIds);


    /**
     * 根据状态获取回收员订单列表
     * @param recyclerId
     * @param status
     * @return
     */
    List<RecyclerOrderVo> getRecyclerOrderListByStatus(Long recyclerId, Integer status);

    /**
     * 回收员接单后，在预约时间前一个小时取消订单时，要重新把订单给别的回收员接单
     * @param orderId
     * @return
     */
    Boolean repost(Long orderId);

    /**
     * 回收员到达回收点
     * @param orderId
     * @return
     */
    Boolean arrive(Long orderId);

    /**
     * 更新订单信息
     * @param updateOrderFrom
     * @return
     */
    Boolean updateOrder(UpdateOrderFrom updateOrderFrom);

    /**
     * 计算实际的订单信息
     * @param orderId
     * @return
     */
    CalculateActualOrderVo calculateActual(Long orderId);

    /**
     * 根据订单id和订单状态修改订单状态
     * @param orderId
     * @param status
     * @return
     */
    Boolean updateOrderStatus(Long orderId, Integer status);


    /**
     * 回收员校验回收码
     * @param validateRecycleCodeForm
     * @return
     */
    Boolean validateRecycleCode(ValidateRecycleCodeForm validateRecycleCodeForm);

    /**
     * 结算订单
     * @param settlementForm
     * @return
     */
    Boolean settlement(SettlementForm settlementForm);

    /***
     * 结算取消订单费用
     * @param cancelOrderForm
     * @return
     */
    CancelOrderFeeVo calculateCancellationFee(CancelOrderFeeForm cancelOrderForm);

    /**
     * 回收员接单后，回收员、顾客取消订单
     * @param cancelOrderForm
     * @return
     */
    Boolean cancelOrderAfterTaking(CancelOrderForm cancelOrderForm);

    /**
     * 删除订单
     * @param orderDeleteForm
     * @return
     */
    Boolean delete(OrderDeleteForm orderDeleteForm);

    /**
     * 获取顾客我的页面的订单初始化信息
     * @param customerId
     * @return
     */
    OrderMyVo getMy(Long customerId);

    /**
     * 后台管理系统汇总数据
     * @return
     */
    CollectVo collect();

    /**
     * 回收员拒收订单
     * @param rejectOrderForm
     * @return
     */
    Boolean reject(RejectOrderForm rejectOrderForm);

    /**
     * 获取拒收订单信息
     * @param orderId
     * @return
     */
    RejectOrderVo getRejectInfo(Long orderId);

    /**
     * 取消申请订单拒收
     * @param orderId
     * @return
     */
    Boolean cancelReject(Long orderId);

    /**
     * 审批拒收申请
     * @param approvalRejectOrderForm
     * @return
     */
    Boolean approvalReject(ApprovalRejectOrderForm approvalRejectOrderForm);


    /**
     * 获取申请拒收订单列表
     * @param page
     * @param rejectOrderListForm
     * @return
     */
    Page<RejectOrderListVo> getRejectOrderList(Page<RejectOrderListVo> page, RejectOrderListForm rejectOrderListForm);
}
