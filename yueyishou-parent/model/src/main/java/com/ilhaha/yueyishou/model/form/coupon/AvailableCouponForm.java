package com.ilhaha.yueyishou.model.form.coupon;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author ilhaha
 * @Create 2024/10/18 14:48
 * @Version 1.0
 * <p>
 * 获取回收员、顾客可使用服务抵扣劵表单类
 */
@Data
public class AvailableCouponForm {

    /**
     * 顾客Id
     */
    private Long customerId;

    /**
     * 回收员Id
     */
    private Long recyclerId;

    /**
     * 回收员实际支付订单金额
     */
    private BigDecimal realRecyclerAmount;


}
