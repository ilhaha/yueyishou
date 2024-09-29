package com.ilhaha.yueyishou.reles.service.impl;

import com.ilhaha.yueyishou.model.form.order.ServiceFeeRuleRequestForm;
import com.ilhaha.yueyishou.model.vo.order.ServiceFeeRuleResponseVo;
import com.ilhaha.yueyishou.reles.service.ServiceFeeRuleService;
import jakarta.annotation.Resource;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.stereotype.Service;

/**
 * @Author ilhaha
 * @Create 2024/9/28 20:12
 * @Version 1.0
 */
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
        response.setEstimatedTotalAmount(serviceFeeRuleRequestForm.getUnitPrice().multiply(serviceFeeRuleRequestForm.getRecycleWeigh()));
        return response;
    }
}
