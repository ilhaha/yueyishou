package com.ilhaha.yueyishou.order.client;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ilhaha.yueyishou.model.entity.order.OrderInfo;
import com.ilhaha.yueyishou.model.form.order.OrderMgrQueryForm;
import com.ilhaha.yueyishou.common.result.Result;
import com.ilhaha.yueyishou.model.vo.order.CustomerOrderDetailsVo;
import com.ilhaha.yueyishou.model.vo.order.CustomerOrderListVo;
import com.ilhaha.yueyishou.model.vo.order.OrderMgrQueryVo;
import com.ilhaha.yueyishou.model.vo.order.PlaceOrderForm;
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
     * 取消订单
     * @param orderId
     * @return
     */
    @PostMapping("/orderInfo/cancel/{orderId}")
    Result<Boolean> cancelOrder(@PathVariable("orderId") Long orderId);

    /**
     * 根据订单ID获取订单详情
     */
    @GetMapping("/orderInfo/details/{orderId}")
    Result<CustomerOrderDetailsVo> getOrderDetails(@PathVariable("orderId") Long orderId);

    /**
     * 根据订单状态获取订单列表
     * @param status
     * @return
     */
    @GetMapping("/orderInfo/list/{status}/{customerId}")
    Result<List<CustomerOrderListVo>> orderList(@PathVariable("status") Integer status,
                                                       @PathVariable("customerId") Long customerId);


    /**
     * 下单
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
                                                @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                @RequestParam(name="pageSize", defaultValue="10") Integer pageSize);


    /**
     * 通过id查询订单信息
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/orderInfo/queryById")
    Result<OrderInfo> queryById(@RequestParam(name="id",required=true) String id);

}
