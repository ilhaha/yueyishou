package com.ilhaha.yueyishou.reles.service;

import com.ilhaha.yueyishou.model.form.order.ServiceFeeRuleRequestForm;
import com.ilhaha.yueyishou.model.vo.order.ServiceFeeRuleResponseVo;

/**
 * @Author ilhaha
 * @Create 2024/9/28 20:12
 * @Version 1.0
 */
public interface ServiceFeeRuleService {

    /**
     * 预估订单费用
     * @param serviceFeeRuleRequestForm
     * @return
     */
    ServiceFeeRuleResponseVo calculateOrderFee(ServiceFeeRuleRequestForm serviceFeeRuleRequestForm);
}
