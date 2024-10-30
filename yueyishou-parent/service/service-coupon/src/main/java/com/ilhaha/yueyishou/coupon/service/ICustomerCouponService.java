package com.ilhaha.yueyishou.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ilhaha.yueyishou.model.entity.coupon.CustomerCoupon;
import com.ilhaha.yueyishou.model.form.coupon.AvailableCouponForm;
import com.ilhaha.yueyishou.model.form.coupon.FreeIssueForm;
import com.ilhaha.yueyishou.model.form.coupon.UseCouponFrom;
import com.ilhaha.yueyishou.model.vo.coupon.AvailableCouponVo;
import com.ilhaha.yueyishou.model.vo.coupon.CouponNotUsedVo;

import java.util.List;

public interface ICustomerCouponService extends IService<CustomerCoupon> {

    /**
     *  免费发放服务抵扣劵
     * @param freeIssueFormList
     * @return
     */
    Boolean freeIssue(List<FreeIssueForm> freeIssueFormList);

    /**
     * 获取顾客在订单中可使用的服务抵扣劵
     * @param availableCouponForm
     * @return
     */
    List<AvailableCouponVo> getAvailableCustomerServiceCoupons(AvailableCouponForm availableCouponForm);

    /**
     * 顾客使用服务抵扣劵
     * @param useCouponFrom
     * @return
     */
    Boolean useCoupon(UseCouponFrom useCouponFrom);

    /**
     * 获取顾客的服务抵扣劵
     * @param customer
     * @return
     */
    List<CouponNotUsedVo> getNotUsedCoupon(Long customerId);
}
