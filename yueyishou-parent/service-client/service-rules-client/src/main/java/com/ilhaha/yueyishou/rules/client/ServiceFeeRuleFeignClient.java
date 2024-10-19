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


}
