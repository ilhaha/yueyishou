package com.ilhaha.yueyishou.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ilhaha.yueyishou.model.entity.coupon.CustomerCoupon;
import com.ilhaha.yueyishou.model.form.coupon.AvailableCouponForm;
import com.ilhaha.yueyishou.model.form.coupon.FreeIssueForm;
import com.ilhaha.yueyishou.model.vo.coupon.AvailableCouponVo;

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

}
