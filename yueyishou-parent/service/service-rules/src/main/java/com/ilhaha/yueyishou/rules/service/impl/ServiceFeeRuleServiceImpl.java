package com.ilhaha.yueyishou.rules.service.impl;
import com.ilhaha.yueyishou.model.form.order.OvertimeRequestForm;
import com.ilhaha.yueyishou.model.form.order.ServiceFeeRuleRequestForm;
import com.ilhaha.yueyishou.model.vo.order.OvertimeResponseVo;
import com.ilhaha.yueyishou.model.vo.order.ServiceFeeRuleResponseVo;
import com.ilhaha.yueyishou.rules.service.ServiceFeeRuleService;
import jakarta.annotation.Resource;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.stereotype.Service;

import java.math.RoundingMode;

@Service
public class ServiceFeeRuleServiceImpl implements ServiceFeeRuleService {
    @Resource
    private KieContainer kieContainer;

    /**
     * 预估订单费用
     * @param serviceFeeRuleRequestForm
     * @return
     */
    @Override
    public ServiceFeeRuleResponseVo calculateOrderFee(ServiceFeeRuleRequestForm serviceFeeRuleRequestForm) {
        // 创建 KieSession
        KieSession kieSession = kieContainer.newKieSession();

        // 创建输出对象
        ServiceFeeRuleResponseVo response = new ServiceFeeRuleResponseVo();

        try {
            // 插入输入对象
            kieSession.insert(serviceFeeRuleRequestForm);
            // 插入输出对象
            kieSession.insert(response);
            // 执行规则
            kieSession.fireAllRules();
        } finally {
            kieSession.dispose();
        }
        // 订单预计回收总金额
        response.setEstimatedTotalAmount(
                serviceFeeRuleRequestForm.getUnitPrice()
                        .multiply(serviceFeeRuleRequestForm.getRecycleWeigh())
                        .setScale(2, RoundingMode.HALF_UP)
        );
        return response;
    }

    /**
     * 计算回收员超时费用
     * @param overtimeRequestForm
     * @return
     */
    @Override
    public OvertimeResponseVo calculateTimeoutFree(OvertimeRequestForm overtimeRequestForm) {
        KieSession kieSession = kieContainer.newKieSession();
        OvertimeResponseVo overtimeResponseVo = new OvertimeResponseVo();

        try {
            kieSession.insert(overtimeRequestForm);
            kieSession.insert(overtimeResponseVo);
            kieSession.fireAllRules(2);
        } finally {
            kieSession.dispose();
        }

        return overtimeResponseVo;
    }
}