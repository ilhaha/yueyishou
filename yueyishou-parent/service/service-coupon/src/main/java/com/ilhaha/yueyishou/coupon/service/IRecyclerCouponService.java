package com.ilhaha.yueyishou.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ilhaha.yueyishou.model.entity.coupon.RecyclerCoupon;
import com.ilhaha.yueyishou.model.form.coupon.FreeIssueForm;

import java.util.List;

public interface IRecyclerCouponService extends IService<RecyclerCoupon> {

    /**
     *  免费发放服务抵扣劵
     * @param freeIssueFormList
     * @param recyclerCount
     * @return
     */
    Boolean freeIssue(List<FreeIssueForm> freeIssueFormList,Integer recyclerCount);

}
