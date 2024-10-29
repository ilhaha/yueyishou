package com.ilhaha.yueyishou.model.form.order;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author ilhaha
 * @Create 2024/10/24 21:21
 * @Version 1.0
 * <p>
 * 接单后取消订单表单类
 */
@Data
public class CancelOrderForm {

    /**
     * 订单Id
     */
    private Long orderId;

    /**
     * 顾客Id
     */
    private Long customerId;

    /**
     * 回收员Id
     */
    private Long recyclerId;

    /**
     * 接单后，回收员在预约时间未到达取消订单赔偿
     */
    private BigDecimal serviceOvertimePenalty;

    /**
     * 接单后，顾客未在免费取消订单时间内取消赔偿
     */
    private BigDecimal customerLateCancellationFee;

    /**
     * 接单后，回收员未在免费取消订单时间内取消赔偿
     */
    private BigDecimal recyclerLateCancellationFee;
}
