package com.ilhaha.yueyishou.rules.client;

import com.ilhaha.yueyishou.common.result.Result;
import com.ilhaha.yueyishou.model.form.order.OvertimeRequestForm;
import com.ilhaha.yueyishou.model.form.order.ServiceFeeRuleRequestForm;
import com.ilhaha.yueyishou.model.vo.order.OvertimeResponseVo;
import com.ilhaha.yueyishou.model.vo.order.ServiceFeeRuleResponseVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @Author ilhaha
 * @Create 2024/9/28 20:19
 * @Version 1.0
 */
@FeignClient("service-rules")
public interface ServiceFeeRuleFeignClient {

    /**
     * 预估订单费用
     * @param serviceFeeRuleRequestForm
     * @return
     */
    @PostMapping("/service/fee/calculateOrderFee")
    Result<ServiceFeeRuleResponseVo> calculateOrderFee(@RequestBody ServiceFeeRuleRequestForm serviceFeeRuleRequestForm);

    /**
     * 计算回收员超时费用
     * @param overtimeRequestForm
     * @return
     */
    @PostMapping("/service/fee/calculateTimeoutFree")
    Result<OvertimeResponseVo> calculateTimeoutFree(@RequestBody OvertimeRequestForm overtimeRequestForm);



    /**
     * 计算回收员服务超时取消（当前时间大于预约上门时间）订单费用
     * @param overtimeRequestForm
     * @return
     */
    @PostMapping("/service/fee/calculateTimeoutCancelFree")
    Result<OvertimeResponseVo> calculateTimeoutCancelFree(@RequestBody OvertimeRequestForm overtimeRequestForm);

    /**
     * 计算顾客超时取消（当前时间超过回收员接单时间五分钟）订单费用
     * @param overtimeRequestForm
     * @return
     */
    @PostMapping("/service/fee/customerLateCancellationFee")
    Result<OvertimeResponseVo> customerLateCancellationFee(@RequestBody OvertimeRequestForm overtimeRequestForm);

    /**
     * 计算回收员超时取消（当前时间距离预约时间不足60分钟）订单费用
     * @param overtimeRequestForm
     * @return
     */
    @PostMapping("/service/fee/recyclerLateCancellationFee")
    Result<OvertimeResponseVo> recyclerLateCancellationFee(@RequestBody OvertimeRequestForm overtimeRequestForm);

    /**
     * 计算回收员拒收得到订单补偿费用
     * @param overtimeRequestForm
     * @return
     */
    @PostMapping("/service/fee/calculateRejectionCompensation")
    Result<OvertimeResponseVo> calculateRejectionCompensation(@RequestBody OvertimeRequestForm overtimeRequestForm);
}
