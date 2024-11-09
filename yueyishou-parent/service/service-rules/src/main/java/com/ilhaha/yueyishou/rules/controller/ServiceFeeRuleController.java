package com.ilhaha.yueyishou.rules.controller;

import com.ilhaha.yueyishou.common.result.Result;
import com.ilhaha.yueyishou.model.form.order.OvertimeRequestForm;
import com.ilhaha.yueyishou.model.form.order.ServiceFeeRuleRequestForm;
import com.ilhaha.yueyishou.model.vo.order.OvertimeResponseVo;
import com.ilhaha.yueyishou.model.vo.order.ServiceFeeRuleResponseVo;
import com.ilhaha.yueyishou.rules.service.ServiceFeeRuleService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

/**
 * @Author ilhaha
 * @Create 2024/9/28 20:09
 * @Version 1.0
 */

@RestController
@RequestMapping("/service/fee")
public class ServiceFeeRuleController {

    @Resource
    private ServiceFeeRuleService serviceFeeRuleService;

    /**
     * 预估订单费用
     * @param serviceFeeRuleRequestForm
     * @return
     */
    @PostMapping("/calculateOrderFee")
    public Result<ServiceFeeRuleResponseVo> calculateOrderFee(@RequestBody ServiceFeeRuleRequestForm serviceFeeRuleRequestForm) {
        return Result.ok(serviceFeeRuleService.calculateOrderFee(serviceFeeRuleRequestForm));
    }


    /**
     * 计算回收员服务超时费用
     * @param overtimeRequestForm
     * @return
     */
    @PostMapping("/calculateTimeoutFree")
    public Result<OvertimeResponseVo> calculateTimeoutFree(@RequestBody OvertimeRequestForm overtimeRequestForm){
        return Result.ok(serviceFeeRuleService.calculateTimeoutFree(overtimeRequestForm));
    }


    /**
     * 计算回收员服务超时取消（当前时间大于预约上门时间）订单费用
     * @param overtimeRequestForm
     * @return
     */
    @PostMapping("/calculateTimeoutCancelFree")
    public Result<OvertimeResponseVo> calculateTimeoutCancelFree(@RequestBody OvertimeRequestForm overtimeRequestForm){
        return Result.ok(serviceFeeRuleService.calculateTimeoutCancelFree(overtimeRequestForm));
    }

    /**
     * 计算顾客超时取消（当前时间超过回收员接单时间五分钟）订单费用
     * @param overtimeRequestForm
     * @return
     */
    @PostMapping("/customerLateCancellationFee")
    public Result<OvertimeResponseVo> customerLateCancellationFee(@RequestBody OvertimeRequestForm overtimeRequestForm){
        return Result.ok(serviceFeeRuleService.customerLateCancellationFee(overtimeRequestForm));
    }


    /**
     * 计算回收员超时取消（当前时间距离预约时间不足60分钟）订单费用
     * @param overtimeRequestForm
     * @return
     */
    @PostMapping("/recyclerLateCancellationFee")
    public Result<OvertimeResponseVo> recyclerLateCancellationFee(@RequestBody OvertimeRequestForm overtimeRequestForm){
        return Result.ok(serviceFeeRuleService.recyclerLateCancellationFee(overtimeRequestForm));
    }

    /**
     * 计算回收员拒收得到订单补偿费用
     * @param overtimeRequestForm
     * @return
     */
    @PostMapping("/calculateRejectionCompensation")
    public Result<OvertimeResponseVo> calculateRejectionCompensation(@RequestBody OvertimeRequestForm overtimeRequestForm){
        return Result.ok(serviceFeeRuleService.calculateRejectionCompensation(overtimeRequestForm));
    }

}
