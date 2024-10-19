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
     * 计算回收员超时费用
     * @param overtimeRequestForm
     * @return
     */
    @PostMapping("/calculateTimeoutFree")
    public Result<OvertimeResponseVo> calculateTimeoutFree(@RequestBody OvertimeRequestForm overtimeRequestForm){
        return Result.ok(serviceFeeRuleService.calculateTimeoutFree(overtimeRequestForm));
    }

}
