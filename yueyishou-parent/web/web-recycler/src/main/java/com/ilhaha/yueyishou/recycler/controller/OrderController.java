package com.ilhaha.yueyishou.recycler.controller;

import com.ilhaha.yueyishou.common.anno.LoginVerification;
import com.ilhaha.yueyishou.common.result.Result;
import com.ilhaha.yueyishou.model.form.order.*;
import com.ilhaha.yueyishou.model.vo.order.*;
import com.ilhaha.yueyishou.recycler.service.OrderService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author ilhaha
 * @Create 2024/10/12 16:41
 * @Version 1.0
 */
@RestController
@RequestMapping("/order")
public class OrderController {

    @Resource
    private OrderService orderService;

    /**
     * 删除订单
     * @param orderDeleteForm
     * @return
     */
    @LoginVerification
    @PostMapping("/delete")
    public Result<Boolean> delete(@RequestBody OrderDeleteForm orderDeleteForm){
        return orderService.delete(orderDeleteForm);
    }

    /**
     * 回收员接单后，回收员、顾客取消订单
     * @param cancelOrderForm
     * @return
     */
    @LoginVerification
    @PostMapping("/cancelOrderAfterTaking")
    public Result<Boolean> cancelOrderAfterTaking(@RequestBody CancelOrderForm cancelOrderForm){
        return orderService.cancelOrderAfterTaking(cancelOrderForm);
    }

    /***
     * 结算取消订单费用
     * @param cancelOrderForm
     * @return
     */
    @LoginVerification
    @PostMapping("/calculateCancellationFee")
    public Result<CancelOrderFeeVo> calculateCancellationFee(@RequestBody CancelOrderFeeForm cancelOrderForm){
        return orderService.calculateCancellationFee(cancelOrderForm);
    }

    /**
     * 结算订单
     * @param settlementForm
     * @return
     */
    @LoginVerification
    @PostMapping("/settlement")
    public Result<Boolean> settlement(@RequestBody SettlementForm settlementForm){
        return orderService.settlement(settlementForm);
    }

    /**
     * 回收员校验回收码
     * @param validateRecycleCodeForm
     * @return
     */
    @LoginVerification
    @PostMapping("/validate/code")
    public Result<Boolean> validateRecycleCode(@RequestBody ValidateRecycleCodeForm validateRecycleCodeForm){
        return orderService.validateRecycleCode(validateRecycleCodeForm);
    }

    /**
     * 计算实际的订单信息
     * @param orderId
     * @return
     */
    @GetMapping("/calculate/actual/{orderId}")
    public Result<CalculateActualOrderVo> calculateActual(@PathVariable("orderId") Long orderId){
        return orderService.calculateActual(orderId);
    }

    /**
     * 更新订单信息
     * @param updateOrderFrom
     * @return
     */
    @LoginVerification
    @PostMapping("/update")
    public Result<Boolean> updateOrder(@RequestBody UpdateOrderFrom updateOrderFrom){
        return orderService.updateOrder(updateOrderFrom);
    }

    /**
     * 预估订单费用
     * @param calculateOrderFeeForm
     * @return
     */
    @LoginVerification
    @PostMapping("/calculateOrderFee")
    public Result<ServiceFeeRuleResponseVo> calculateOrderFee(@RequestBody ServiceFeeRuleRequestForm calculateOrderFeeForm) {
        return orderService.calculateOrderFee(calculateOrderFeeForm);
    }

    /**
     * 回收员到达回收点
     * @param orderId
     * @return
     */
    @LoginVerification
    @PostMapping("/arrive/{orderId}")
    public Result<Boolean> arrive(@PathVariable("orderId") Long orderId){
        return orderService.arrive(orderId);
    }

    /**
     * 回收员接单后，在预约时间前一个小时取消订单时，要重新把订单给别的回收员接单
     * @param orderId
     * @return
     */
    @LoginVerification
    @PostMapping("/repost/{orderId}")
    public Result<Boolean> repost(@PathVariable("orderId") Long orderId){
        return orderService.repost(orderId);
    }


    /**
     * 根据状态获取回收员订单列表
     * @param status
     * @return
     */
    @LoginVerification
    @GetMapping("/list/{status}")
    public Result<List<RecyclerOrderVo>> getRecyclerOrderListByStatus(@PathVariable("status") Integer status){
        return orderService.getRecyclerOrderListByStatus(status);
    }

    /**
     * 回收员抢单
     * @param orderId
     * @return
     */
    @LoginVerification
    @PostMapping("/grab/{orderId}")
    public Result<Boolean> grabOrder(@PathVariable("orderId") Long orderId){
        return orderService.grabOrder(orderId);
    }

    /**
     * 回收员根据订单ID获取订单详情
     */
    @GetMapping("/details/{orderId}")
    @LoginVerification
    public Result<OrderDetailsVo> getOrderDetails(@PathVariable("orderId") Long orderId) {
        return orderService.getOrderDetails(orderId);
    }

    /**
     * 回收员获取符合接单的订单
     * @param matchingOrderForm
     * @return
     */
    @LoginVerification
    @PostMapping("/matching")
    public Result<List<MatchingOrderVo>> retrieveMatchingOrders(@RequestBody MatchingOrderForm matchingOrderForm){
        return orderService.retrieveMatchingOrders(matchingOrderForm);
    }
}
