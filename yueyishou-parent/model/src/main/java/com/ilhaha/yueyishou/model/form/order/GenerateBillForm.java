package com.ilhaha.yueyishou.model.form.order;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author ilhaha
 * @Create 2024/10/19 10:45
 * @Version 1.0
 *
 * 生成账单表单类
 */
@Data
public class GenerateBillForm {

    /** 订单ID */
    private Long orderId;

    /**回收员Id*/
    private Long recyclerId;

    /**服务抵扣劵Id*/
    private Long couponId;

    /** 订单实际回收总金额 */
    private BigDecimal realOrderRecycleAmount;

    /** 回收员实际支付订单金额 */
    private BigDecimal realRecyclerAmount;

    /** 回收员实际支付平台订单金额 */
    private BigDecimal realRecyclerPlatformAmount;

    /** 回收员开始服务超时费用 */
    private BigDecimal recyclerOvertimeCharges;

    /** 回收员服务抵扣券抵扣金额 */
    private BigDecimal recyclerCouponAmount;

    /**顾客实际支付平台订单金额*/
    private BigDecimal realCustomerPlatformAmount;
}
