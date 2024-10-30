package com.ilhaha.yueyishou.customer.service;

import com.ilhaha.yueyishou.common.result.Result;
import com.ilhaha.yueyishou.model.vo.coupon.CouponNotUsedVo;

import java.util.List;

/**
 * @Author ilhaha
 * @Create 2024/10/29 23:05
 * @Version 1.0
 */
public interface CouponService {

    /**
     * 获取顾客的服务抵扣劵
     * @return
     */
    Result<List<CouponNotUsedVo>> getNotUsedCoupon();

}
