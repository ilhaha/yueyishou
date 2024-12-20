package com.ilhaha.yueyishou.order.client;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ilhaha.yueyishou.model.entity.order.OrderInfo;
import com.ilhaha.yueyishou.model.form.order.*;
import com.ilhaha.yueyishou.common.result.Result;
import com.ilhaha.yueyishou.model.vo.order.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author ilhaha
 * @Create 2024/9/11 14:29
 * @Version 1.0
 */
@FeignClient("service-order")
public interface OrderInfoFeignClient {

    /**
     * 审批拒收申请
     *
     * @param approvalRejectOrderForm
     * @return
     */
    @PostMapping("/orderInfo/approval/reject")
    Result<Boolean> approvalReject(@RequestBody ApprovalRejectOrderForm approvalRejectOrderForm);

    /**
     * 获取申请拒收订单列表
     * @param pageNo
     * @param pageSize
     * @param rejectOrderListForm
     * @return
     */
    @PostMapping("/orderInfo/reject/list")
    Result<Page<RejectOrderListVo>> getRejectOrderList( @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                               @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                               @RequestBody RejectOrderListForm rejectOrderListForm);
    /**
     * 取消申请订单拒收
     *
     * @param orderId
     * @return
     */
    @PostMapping("/orderInfo/cancel/reject/{orderId}")
    Result<Boolean> cancelReject(@PathVariable("orderId") Long orderId);

    /**
     * 获取拒收订单信息
     *
     * @param orderId
     * @return
     */
    @GetMapping("/orderInfo/reject/info/{orderId}")
    Result<RejectOrderVo> getRejectInfo(@PathVariable Long orderId);

    /**
     * 回收员拒收订单
     *
     * @param rejectOrderForm
     * @return
     */
    @PostMapping("/orderInfo/reject")
    Result<Boolean> reject(@RequestBody RejectOrderForm rejectOrderForm);

    /**
     * 后台管理系统汇总数据
     *
     * @return
     */
    @GetMapping("/orderInfo/index/collect")
    Result<CollectVo> collect();

    /**
     * 获取顾客我的页面的订单初始化信息
     *
     * @param customerId
     * @return
     */
    @GetMapping("/orderInfo/my/{customerId}")
    Result<OrderMyVo> getMy(@PathVariable Long customerId);

    /**
     * 删除订单
     *
     * @param orderDeleteForm
     * @return
     */
    @PostMapping("/orderInfo/delete")
    Result<Boolean> delete(@RequestBody OrderDeleteForm orderDeleteForm);

    /**
     * 回收员接单后，回收员、顾客取消订单
     *
     * @param cancelOrderForm
     * @return
     */
    @PostMapping("/orderInfo/cancelOrderAfterTaking")
    Result<Boolean> cancelOrderAfterTaking(@RequestBody CancelOrderForm cancelOrderForm);

    /***
     * 结算取消订单费用
     * @param cancelOrderForm
     * @return
     */
    @PostMapping("/orderInfo/calculateCancellationFee")
    Result<CancelOrderFeeVo> calculateCancellationFee(@RequestBody CancelOrderFeeForm cancelOrderForm);

    /**
     * 结算订单
     *
     * @param settlementForm
     * @return
     */
    @PostMapping("/orderInfo/settlement")
    Result<Boolean> settlement(@RequestBody SettlementForm settlementForm);

    /**
     * 回收员校验回收码
     *
     * @param validateRecycleCodeForm
     * @return
     */
    @PostMapping("/orderInfo/validate/code")
    Result<Boolean> validateRecycleCode(@RequestBody ValidateRecycleCodeForm validateRecycleCodeForm);

    /**
     * 计算实际的订单信息
     *
     * @param orderId
     * @return
     */
    @GetMapping("/orderInfo/calculate/actual/{orderId}")
    Result<CalculateActualOrderVo> calculateActual(@PathVariable("orderId") Long orderId);

    /**
     * 更新订单信息
     *
     * @param updateOrderFrom
     * @return
     */
    @PostMapping("/orderInfo/update")
    Result<Boolean> updateOrder(@RequestBody UpdateOrderFrom updateOrderFrom);

    /**
     * 回收员到达回收点
     *
     * @param orderId
     * @return
     */
    @PostMapping("/orderInfo/arrive/{orderId}")
    Result<Boolean> arrive(@PathVariable("orderId") Long orderId);


    /**
     * 回收员接单后，在预约时间前一个小时取消订单时，要重新把订单给别的回收员接单
     *
     * @param orderId
     * @return
     */
    @PostMapping("/orderInfo/repost/{orderId}")
    Result<Boolean> repost(@PathVariable("orderId") Long orderId);

    /**
     * 根据状态获取回收员订单列表
     *
     * @param recyclerId
     * @param status
     * @return
     */
    @GetMapping("/orderInfo/list/status/{recyclerId}/{status}")
    Result<List<RecyclerOrderVo>> getRecyclerOrderListByStatus(@PathVariable("recyclerId") Long recyclerId,
                                                               @PathVariable("status") Integer status);

    /**
     * 取消预约时间超时的订单
     *
     * @return
     */
    @PostMapping("/orderInfo/processTimeoutOrders")
    Result<Boolean> processTimeoutOrders(@RequestBody List<Long> timeoutOrderIds);

    /**
     * 回收员抢单
     *
     * @param recyclerId
     * @param orderId
     * @return
     */
    @PostMapping("/orderInfo/grab/{recyclerId}/{orderId}")
    Result<Boolean> grabOrder(@PathVariable("recyclerId") Long recyclerId,
                              @PathVariable("orderId") Long orderId);

    /**
     * 回收员获取符合接单的订单
     *
     * @param matchingOrderForm
     * @return
     */
    @PostMapping("/orderInfo/matching")
    Result<List<MatchingOrderVo>> retrieveMatchingOrders(@RequestBody MatchingOrderForm matchingOrderForm);


    /**
     * 取消订单
     *
     * @param orderId
     * @return
     */
    @PostMapping("/orderInfo/cancel/{orderId}")
    Result<Boolean> cancelOrder(@PathVariable("orderId") Long orderId);

    /**
     * 顾客根据订单ID获取订单详情
     */
    @GetMapping("/orderInfo/details/{orderId}")
    Result<OrderDetailsVo> getOrderDetails(@PathVariable("orderId") Long orderId);

    /**
     * 回收员根据订单ID获取订单详情
     *
     * @param recyclerId
     * @param orderId
     * @return
     */
    @GetMapping("/orderInfo/details/{recyclerId}/{orderId}")
    Result<OrderDetailsVo> getOrderDetails(@PathVariable("recyclerId") Long recyclerId,
                                           @PathVariable("orderId") Long orderId);

    /**
     * 根据订单状态获取订单列表
     *
     * @param status
     * @return
     */
    @GetMapping("/orderInfo/list/{status}/{customerId}")
    Result<List<CustomerOrderListVo>> orderList(@PathVariable("status") Integer status,
                                                @PathVariable("customerId") Long customerId);


    /**
     * 下单
     *
     * @param placeOrderForm
     * @return
     */
    @PostMapping("/orderInfo/place")
    Result<Boolean> placeOrder(@RequestBody PlaceOrderForm placeOrderForm);

    /**
     * 订单分页列表查询
     *
     * @param orderMgrQueryForm
     * @param pageNo
     * @param pageSize
     * @return
     */
    @PostMapping(value = "/orderInfo/list")
    Result<Page<OrderMgrQueryVo>> queryPageList(@RequestBody OrderMgrQueryForm orderMgrQueryForm,
                                                @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize);


    /**
     * 通过id查询订单信息
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/orderInfo/queryById")
    Result<OrderInfo> queryById(@RequestParam(name = "id", required = true) String id);

}
