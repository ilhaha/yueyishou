package com.ilhaha.yueyishou.model.vo.coupon;

import lombok.Data;

/**
 * @Author ilhaha
 * @Create 2024/11/7 14:50
 * @Version 1.0
 *
 * 已拥有的服务抵扣劵Vo
 */
@Data
public class ExistingCouponVo {

    /**服务费抵扣券ID*/
    private Long couponId;
    /**回收员ID*/
    private Long recyclerId;
    /**优化券状态（1：未使用 2：已使用）*/
}
