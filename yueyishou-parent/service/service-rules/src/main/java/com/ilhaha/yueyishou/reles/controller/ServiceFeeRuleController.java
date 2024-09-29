package com.ilhaha.yueyishou.reles.controller;

import com.ilhaha.yueyishou.common.result.Result;
import com.ilhaha.yueyishou.model.form.order.ServiceFeeRuleRequestForm;
import com.ilhaha.yueyishou.model.vo.order.ServiceFeeRuleResponseVo;
import com.ilhaha.yueyishou.reles.service.ServiceFeeRuleService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
