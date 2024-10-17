package com.ilhaha.yueyishou.coupon.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ilhaha.yueyishou.coupon.mapper.RecyclerCouponMapper;
import com.ilhaha.yueyishou.coupon.service.ICouponInfoService;
import com.ilhaha.yueyishou.coupon.service.IRecyclerCouponService;
import com.ilhaha.yueyishou.model.entity.coupon.RecyclerCoupon;
import com.ilhaha.yueyishou.model.form.coupon.FreeIssueForm;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecyclerCouponServiceImpl extends ServiceImpl<RecyclerCouponMapper, RecyclerCoupon> implements IRecyclerCouponService {

    @Resource
    private ICouponInfoService couponInfoService;

    /**
     *  免费发放服务抵扣劵
     * @param freeIssueFormList
     * @param recyclerCount
     * @return
     */
    @Override
    public Boolean freeIssue(List<FreeIssueForm> freeIssueFormList,Integer recyclerCount) {
        List<RecyclerCoupon> inertRecyclerCouponList = freeIssueFormList.stream().map(item -> {
            RecyclerCoupon recyclerCoupon = new RecyclerCoupon();
            BeanUtils.copyProperties(item, recyclerCoupon);
            return recyclerCoupon;
        }).collect(Collectors.toList());
        for (int i = 0; i < recyclerCount; i++) {
            // 更新优惠价的领取数量
            couponInfoService.updateCouponReceiveQuantity(freeIssueFormList.stream().map(item ->{
                return item.getCouponId();
            }).collect(Collectors.toList()));
        }
        return this.saveBatch(inertRecyclerCouponList);
    }
}
