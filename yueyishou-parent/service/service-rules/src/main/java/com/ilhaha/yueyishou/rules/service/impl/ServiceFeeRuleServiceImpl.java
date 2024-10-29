package com.ilhaha.yueyishou.rules.service.impl;

import com.ilhaha.yueyishou.model.constant.RulesConstant;
import com.ilhaha.yueyishou.model.form.order.OvertimeRequestForm;
import com.ilhaha.yueyishou.model.form.order.ServiceFeeRuleRequestForm;
import com.ilhaha.yueyishou.model.vo.order.OvertimeResponseVo;
import com.ilhaha.yueyishou.model.vo.order.ServiceFeeRuleResponseVo;
import com.ilhaha.yueyishou.rules.service.ServiceFeeRuleService;
import org.kie.api.KieBase;
import org.kie.api.io.Resource;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieSession;
import org.kie.internal.io.ResourceFactory;
import org.kie.internal.utils.KieHelper;
import org.springframework.stereotype.Service;

import java.math.RoundingMode;

@Service
public class ServiceFeeRuleServiceImpl implements ServiceFeeRuleService {

    /**
     * 预估订单费用
     *
     * @param serviceFeeRuleRequestForm
     * @return
     */
    @Override
    public ServiceFeeRuleResponseVo calculateOrderFee(ServiceFeeRuleRequestForm serviceFeeRuleRequestForm) {

        KieHelper kieHelper = new KieHelper();

        // 加载第一个 DRL 文件
        Resource resource1 = ResourceFactory.newClassPathResource(RulesConstant.CUSTOMER_SERVICE_FEE_RULE,RulesConstant.ENCODING);
        kieHelper.addResource(resource1, ResourceType.DRL);

        // 加载第二个 DRL 文件
        Resource resource2 = ResourceFactory.newClassPathResource(
                RulesConstant.RECYCLER_SERVICE_FEE_RULE, RulesConstant.ENCODING);
        kieHelper.addResource(resource2, ResourceType.DRL);

        // 构建 KieBase 并创建 KieSession
        KieBase kieBase = kieHelper.build();
        KieSession kieSession = kieBase.newKieSession();

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
            // 释放资源
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
     * 计算回收员服务超时费用
     *
     * @param overtimeRequestForm
     * @return
     */
    @Override
    public OvertimeResponseVo calculateTimeoutFree(OvertimeRequestForm overtimeRequestForm) {
        KieHelper kieHelper = new KieHelper();
        // 获取指定的drl文件
        Resource resource = ResourceFactory.newClassPathResource(RulesConstant.RECYCLER_OVERTIME_FEE_RULE, RulesConstant.ENCODING);
        kieHelper.addResource(resource, ResourceType.DRL);
        KieBase kieBase = kieHelper.build();
        KieSession kieSession = kieBase.newKieSession();
        OvertimeResponseVo overtimeResponseVo = new OvertimeResponseVo();
        try {
            kieSession.insert(overtimeRequestForm);
            kieSession.insert(overtimeResponseVo);
            kieSession.fireAllRules();
        } finally {
            kieSession.dispose();
        }

        return overtimeResponseVo;
    }

    /**
     * 计算回收员服务超时取消（当前时间大于预约上门时间）订单费用
     * @param overtimeRequestForm
     * @return
     */
    @Override
    public OvertimeResponseVo calculateTimeoutCancelFree(OvertimeRequestForm overtimeRequestForm) {
        KieHelper kieHelper = new KieHelper();
        // 获取指定的drl文件
        Resource resource = ResourceFactory.newClassPathResource(RulesConstant.RECYCLER_OVERTIME_CANCEL_FEE_RULE, RulesConstant.ENCODING);
        kieHelper.addResource(resource, ResourceType.DRL);
        KieBase kieBase = kieHelper.build();
        KieSession kieSession = kieBase.newKieSession();
        OvertimeResponseVo overtimeResponseVo = new OvertimeResponseVo();
        try {
            kieSession.insert(overtimeRequestForm);
            kieSession.insert(overtimeResponseVo);
            kieSession.fireAllRules();
        } finally {
            kieSession.dispose();
        }

        return overtimeResponseVo;
    }

    /**
     * 计算顾客超时取消（当前时间超过回收员接单时间五分钟）订单费用
     * @param overtimeRequestForm
     * @return
     */
    @Override
    public OvertimeResponseVo customerLateCancellationFee(OvertimeRequestForm overtimeRequestForm) {
        KieHelper kieHelper = new KieHelper();
        // 获取指定的drl文件
        Resource resource = ResourceFactory.newClassPathResource(RulesConstant.CUSTOMER_LATE_CANCELLATION_FEE_RULE, RulesConstant.ENCODING);
        kieHelper.addResource(resource, ResourceType.DRL);
        KieBase kieBase = kieHelper.build();
        KieSession kieSession = kieBase.newKieSession();
        OvertimeResponseVo overtimeResponseVo = new OvertimeResponseVo();
        try {
            kieSession.insert(overtimeRequestForm);
            kieSession.insert(overtimeResponseVo);
            kieSession.fireAllRules();
        } finally {
            kieSession.dispose();
        }

        return overtimeResponseVo;
    }

    /**
     * 计算回收员超时取消（当前时间距离预约时间不足60分钟）订单费用
     * @param overtimeRequestForm
     * @return
     */
    @Override
    public OvertimeResponseVo recyclerLateCancellationFee(OvertimeRequestForm overtimeRequestForm) {
        KieHelper kieHelper = new KieHelper();
        // 获取指定的drl文件
        Resource resource = ResourceFactory.newClassPathResource(RulesConstant.RECYCLER_LATE_CANCELLATION_FEE_RULE, RulesConstant.ENCODING);
        kieHelper.addResource(resource, ResourceType.DRL);
        KieBase kieBase = kieHelper.build();
        KieSession kieSession = kieBase.newKieSession();
        OvertimeResponseVo overtimeResponseVo = new OvertimeResponseVo();
        try {
            kieSession.insert(overtimeRequestForm);
            kieSession.insert(overtimeResponseVo);
            kieSession.fireAllRules();
        } finally {
            kieSession.dispose();
        }

        return overtimeResponseVo;
    }
}