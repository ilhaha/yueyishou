package com.ilhaha.yueyishou.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ilhaha.yueyishou.model.entity.coupon.RecyclerCoupon;
import com.ilhaha.yueyishou.model.form.coupon.AvailableCouponForm;
import com.ilhaha.yueyishou.model.form.coupon.FreeIssueForm;
import com.ilhaha.yueyishou.model.form.coupon.UseCouponFrom;
import com.ilhaha.yueyishou.model.vo.coupon.AvailableCouponVo;
import com.ilhaha.yueyishou.model.vo.coupon.CouponNotUsedVo;
import com.ilhaha.yueyishou.model.vo.coupon.ExistingCouponVo;

import java.util.List;

public interface IRecyclerCouponService extends IService<RecyclerCoupon> {

    /**
     *  免费发放服务抵扣劵
     * @param freeIssueFormList
     * @param recyclerCount
     * @return
     */
    Boolean freeIssue(List<FreeIssueForm> freeIssueFormList,Integer recyclerCount);

    /**
     * 获取回收员在订单中可使用的服务抵扣劵
     * @param availableCouponForm
     * @return
     */
    List<AvailableCouponVo> getAvailableCustomerServiceCoupons(AvailableCouponForm availableCouponForm);

    /**
     * 回收员使用服务抵扣劵
     * @param useCouponFrom
     * @return
     */
    Boolean useCoupon(UseCouponFrom useCouponFrom);

    /**
     * 获取回收员的服务抵扣劵
     * @param recyclerId
     * @return
     */
    List<CouponNotUsedVo> getNotUsedCoupon(Long recyclerId);

    /**
     * 获取回收员已有的服务抵扣劵
     * @param recyclerIds
     * @return
     */
    List<ExistingCouponVo> getExistingCoupons(List<Long> recyclerIds);
}
