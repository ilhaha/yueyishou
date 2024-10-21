package com.ilhaha.yueyishou.model.form.coupon;

import lombok.Data;

import java.util.Date;

/**
 * @Author ilhaha
 * @Create 2024/10/19 11:22
 * @Version 1.0
 *
 * 使用服务抵扣劵的表单类
 */
@Data
public class UseCouponFrom {

    /**回收员ID*/
    private Long recyclerId;
    /**顾客ID*/
    private Long customerId;
    /**服务抵扣劵Id*/
    private Long couponId;
    /**优化券状态（1：未使用 2：已使用）*/
    private Integer status;
    /**使用时间*/
    private Date usedTime;
    /**使用的订单id*/
    private Long orderId;
}
