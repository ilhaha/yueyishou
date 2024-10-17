package com.ilhaha.yueyishou.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ilhaha.yueyishou.model.entity.coupon.CustomerCoupon;
import com.ilhaha.yueyishou.model.form.coupon.FreeIssueForm;

import java.util.List;

public interface ICustomerCouponService extends IService<CustomerCoupon> {

    /**
     *  免费发放服务抵扣劵
     * @param freeIssueFormList
     * @return
     */
    Boolean freeIssue(List<FreeIssueForm> freeIssueFormList);
}
