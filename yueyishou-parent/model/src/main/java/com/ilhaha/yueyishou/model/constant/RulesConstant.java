package com.ilhaha.yueyishou.model.constant;

/**
 * @Author ilhaha
 * @Create 2024/10/25 14:30
 * @Version 1.0
 *
 * Drools规则文件路径
 */
public class RulesConstant {

    /**
     * 字符集
     */
    public static final String ENCODING = "UTF-8";

    /**
     * 平台收取顾客服务费规则文件
     */
    public static final String CUSTOMER_SERVICE_FEE_RULE = "rules/CustomerServiceFeeRule.drl";

    /**
     * 平台收取回收员服务费规则文件
     */
    public static final String RECYCLER_SERVICE_FEE_RULE = "rules/RecyclerServiceFeeRule.drl";

    /**
     * 回收员上门超时赔偿规则文件
     */
    public static final String RECYCLER_OVERTIME_FEE_RULE = "rules/RecyclerOvertimeFeeRule.drl";

    /**
     * 顾客、回收员在回收员已过预约时间时取消订单回收员赔偿规则文件
     */
    public static final String RECYCLER_OVERTIME_CANCEL_FEE_RULE = "rules/RecyclerOvertimeCancelFeeRule.drl";

    /**
     * 顾客在回收员已接单情况下，超过接单五分钟后赔偿规则文件
     */
    public static final String CUSTOMER_LATE_CANCELLATION_FEE_RULE = "rules/CustomerLateCancellationFeeRule.drl";

    /**
     * 回收员已接单情况下，离预约时间不足60分钟赔偿规则文件
     */
    public static final String RECYCLER_LATE_CANCELLATION_FEE_RULE = "rules/RecyclerLateCancellationFeeRule.drl";
}
