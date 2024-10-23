package com.ilhaha.yueyishou.model.form.order;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author ilhaha
 * @Create 2024/10/19 17:03
 * @Version 1.0
 * <p>
 * 修改订单账单表单类
 */
@Data
public class UpdateBillForm {

    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 顾客Id
     */
    private Long customerId;

    /**
     * 服务抵扣劵Id
     */
    private Long couponId;

    /**
     * 顾客实际回收订单金额
     */
    private BigDecimal realCustomerRecycleAmount;

    /**
     * 顾客实际支付平台订单金额
     */
    private BigDecimal realCustomerPlatformAmount;

    /**
     * 顾客服务抵扣券抵扣金额
     */
    private BigDecimal customerCouponAmount;

    /**
     * 支付订单号
     */
    private String transactionId;

    /**
     * 回收员付款时间
     */
    private Date payTime;


}
