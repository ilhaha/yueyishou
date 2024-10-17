package com.ilhaha.yueyishou.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ilhaha.yueyishou.model.entity.coupon.CouponInfo;

import java.util.List;

public interface ICouponInfoService extends IService<CouponInfo> {

    /**
     * 通过id集合获取服务抵扣劵集合
     * @param couponIds
     * @return
     */
    List<CouponInfo> getListByIds(List<Long> couponIds);

    /**
     * 更新服务抵扣劵领取数量
     * @param couponIds
     */
    void updateCouponReceiveQuantity(List<Long> couponIds);
}
