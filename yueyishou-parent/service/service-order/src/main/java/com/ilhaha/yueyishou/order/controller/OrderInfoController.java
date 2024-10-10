package com.ilhaha.yueyishou.order.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ilhaha.yueyishou.common.execption.YueYiShouException;
import com.ilhaha.yueyishou.model.entity.order.OrderInfo;
import com.ilhaha.yueyishou.model.form.order.ServiceFeeRuleRequestForm;
import com.ilhaha.yueyishou.model.form.order.OrderMgrQueryForm;
import com.ilhaha.yueyishou.model.vo.order.*;
import com.ilhaha.yueyishou.order.service.IOrderInfoService;
import com.ilhaha.yueyishou.common.result.Result;
import com.ilhaha.yueyishou.common.result.ResultCodeEnum;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/orderInfo")
@Slf4j
public class OrderInfoController {
    @Resource
    private IOrderInfoService orderInfoService;

    /**
     * 取消订单
     * @param orderId
     * @return
     */
    @PostMapping("/cancel/{orderId}")
    public Result<Boolean> cancelOrder(@PathVariable("orderId") Long orderId){
        return Result.ok(orderInfoService.cancelOrder(orderId));
    }

	/**
	 * 根据订单ID获取订单详情
	 */
    @GetMapping("/details/{orderId}")
    public Result<CustomerOrderDetailsVo> getOrderDetails(@PathVariable("orderId") Long orderId) {
		return Result.ok(orderInfoService.getOrderDetails(orderId));
    }

    /**
     * 根据订单状态获取订单列表
     *
     * @param status
     * @return
     */
    @GetMapping("/list/{status}/{customerId}")
    public Result<List<CustomerOrderListVo>> orderList(@PathVariable("status") Integer status,
                                                       @PathVariable("customerId") Long customerId) {
        return Result.ok(orderInfoService.orderList(status, customerId));
    }

    /**
     * 下单
     *
     * @param placeOrderForm
     * @return
     */
    @PostMapping("/place")
    public Result<Boolean> placeOrder(@RequestBody PlaceOrderForm placeOrderForm) {
        return Result.ok(orderInfoService.placeOrder(placeOrderForm));
    }

    /**
     * 订单分页列表查询
     *
     * @param orderMgrQueryForm
     * @param pageNo
     * @param pageSize
     * @return
     */
    @PostMapping(value = "/list")
    public Result<Page<OrderMgrQueryVo>> queryPageList(@RequestBody OrderMgrQueryForm orderMgrQueryForm,
                                                       @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                       @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        Page<OrderMgrQueryVo> page = new Page<>(pageNo, pageSize);
        return Result.ok(orderInfoService.queryPageList(orderMgrQueryForm, page));
    }

    /**
     * 添加
     *
     * @param orderInfo
     * @return
     */
    @PostMapping(value = "/add")
    public Result<String> add(@RequestBody OrderInfo orderInfo) {
        orderInfoService.save(orderInfo);
        return Result.ok("添加成功！");
    }

    /**
     * 编辑
     *
     * @param orderInfo
     * @return
     */
    @RequestMapping(value = "/edit", method = {RequestMethod.PUT, RequestMethod.POST})
    public Result<String> edit(@RequestBody OrderInfo orderInfo) {
        orderInfoService.updateById(orderInfo);
        return Result.ok("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @DeleteMapping(value = "/delete")
    public Result<String> delete(@RequestParam(name = "id", required = true) String id) {
        orderInfoService.removeById(id);
        return Result.ok("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @DeleteMapping(value = "/deleteBatch")
    public Result<String> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.orderInfoService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.ok("批量删除成功!");
    }

    /**
     * 通过id查询订单信息
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/queryById")
    public Result<OrderInfo> queryById(@RequestParam(name = "id", required = true) String id) {
        OrderInfo orderInfo = orderInfoService.getById(id);
        if (orderInfo == null) {
            throw new YueYiShouException(ResultCodeEnum.DATA_ERROR);
        }
        return Result.ok(orderInfo);
    }
}
