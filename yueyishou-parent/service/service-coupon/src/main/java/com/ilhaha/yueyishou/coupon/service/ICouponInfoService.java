package com.ilhaha.yueyishou.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ilhaha.yueyishou.model.entity.coupon.CouponInfo;
import com.ilhaha.yueyishou.model.vo.coupon.AvailableCouponVo;

import java.math.BigDecimal;
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

    /**
     * 获取可使用的服务抵扣劵
     * @param couponInfoList
     * @param realRecyclerAmount
     * @return
     */
    List<AvailableCouponVo> getAvailableCoupon(List<CouponInfo> couponInfoList, BigDecimal realRecyclerAmount);

    /**
     * 修改服务抵扣劵的使用数量
     * @param couponId
     * @return
     */
    Boolean updateUseCount(Long couponId);
}
