package com.ilhaha.yueyishou.order.controller;

import com.ilhaha.yueyishou.common.result.Result;
import com.ilhaha.yueyishou.model.entity.order.OrderRejectionOperate;
import com.ilhaha.yueyishou.model.vo.order.RejectInfoVo;
import com.ilhaha.yueyishou.order.service.IOrderRejectionOperateService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

/**
 * @Author ilhaha
 * @Create 2024/11/19 11:47
 * @Version 1.0
 */
@RestController
@RequestMapping("/orderRejectionOperate")
public class OrderRejectionOperateController {

    @Resource
    private IOrderRejectionOperateService orderRejectionOperateService;

    /**
     * 获取回收员申请拒收订单被驳回反馈信息
     * @param orderId
     * @return
     */
    @GetMapping("/reject/feedback/{orderId}")
    public Result<RejectInfoVo> rejectFeedback(@PathVariable("orderId") Long orderId){
        return Result.ok(orderRejectionOperateService.rejectFeedback(orderId));
    }

    /**
     * 添加审核订单拒收操作记录
     * @param rejectionOperate
     * @return
     */
    @PostMapping("/add")
    public Result<Boolean> add(@RequestBody OrderRejectionOperate rejectionOperate){
        return Result.ok(orderRejectionOperateService.add(rejectionOperate));
    }
}
