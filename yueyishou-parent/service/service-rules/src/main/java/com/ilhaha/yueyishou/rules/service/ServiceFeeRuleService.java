package com.ilhaha.yueyishou.rules.service;

import com.ilhaha.yueyishou.model.form.order.OvertimeRequestForm;
import com.ilhaha.yueyishou.model.form.order.ServiceFeeRuleRequestForm;
import com.ilhaha.yueyishou.model.vo.order.OvertimeResponseVo;
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

    /**
     * 计算回收员服务超时费用
     * @param overtimeRequestForm
     * @return
     */
    OvertimeResponseVo calculateTimeoutFree(OvertimeRequestForm overtimeRequestForm);

    /**
     * 计算回收员服务超时取消（当前时间大于预约上门时间）订单费用
     * @param overtimeRequestForm
     * @return
     */
    OvertimeResponseVo calculateTimeoutCancelFree(OvertimeRequestForm overtimeRequestForm);

    /**
     * 计算顾客超时取消（当前时间超过回收员接单时间五分钟）订单费用
     * @param overtimeRequestForm
     * @return
     */
    OvertimeResponseVo customerLateCancellationFee(OvertimeRequestForm overtimeRequestForm);

    /**
     * 计算回收员超时取消（当前时间距离预约时间不足60分钟）订单费用
     * @param overtimeRequestForm
     * @return
     */
    OvertimeResponseVo recyclerLateCancellationFee(OvertimeRequestForm overtimeRequestForm);

    /**
     * 计算回收员拒收得到订单补偿费用
     * @param overtimeRequestForm
     * @return
     */
    OvertimeResponseVo calculateRejectionCompensation(OvertimeRequestForm overtimeRequestForm);
}
