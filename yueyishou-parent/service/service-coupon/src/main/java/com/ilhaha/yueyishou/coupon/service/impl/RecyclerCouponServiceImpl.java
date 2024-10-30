package com.ilhaha.yueyishou.coupon.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ilhaha.yueyishou.coupon.mapper.RecyclerCouponMapper;
import com.ilhaha.yueyishou.coupon.service.ICouponInfoService;
import com.ilhaha.yueyishou.coupon.service.IRecyclerCouponService;
import com.ilhaha.yueyishou.model.constant.CouponConstant;
import com.ilhaha.yueyishou.model.entity.coupon.CouponInfo;
import com.ilhaha.yueyishou.model.entity.coupon.CustomerCoupon;
import com.ilhaha.yueyishou.model.entity.coupon.RecyclerCoupon;
import com.ilhaha.yueyishou.model.form.coupon.AvailableCouponForm;
import com.ilhaha.yueyishou.model.form.coupon.FreeIssueForm;
import com.ilhaha.yueyishou.model.form.coupon.UseCouponFrom;
import com.ilhaha.yueyishou.model.vo.coupon.AvailableCouponVo;
import com.ilhaha.yueyishou.model.vo.coupon.CouponNotUsedVo;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.util.*;
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

    /**
     * 获取回收员在订单中可使用的服务抵扣劵
     * @param availableCouponForm
     * @return
     */
    @Override
    public List<AvailableCouponVo> getAvailableCustomerServiceCoupons(AvailableCouponForm availableCouponForm) {
        List<AvailableCouponVo> result = new ArrayList<>();
        // 查出回收员所有未使用的服务抵扣券
        LambdaQueryWrapper<RecyclerCoupon> recyclerCouponLambdaQueryWrapper = new LambdaQueryWrapper<>();
        recyclerCouponLambdaQueryWrapper.eq(RecyclerCoupon::getStatus, CouponConstant.UNUSED_STATUS)
                .eq(RecyclerCoupon::getRecyclerId, availableCouponForm.getRecyclerId())
                .and(wrapper ->
                        wrapper.isNull(RecyclerCoupon::getExpireTime)
                                .or()
                                .gt(RecyclerCoupon::getExpireTime, LocalDateTime.now()));
        List<RecyclerCoupon> recyclerCouponListDB = this.list(recyclerCouponLambdaQueryWrapper);

        if (!ObjectUtils.isEmpty(recyclerCouponListDB)) {
            List<Long> couponIds = recyclerCouponListDB.stream()
                    .map(RecyclerCoupon::getCouponId)
                    .collect(Collectors.toList());

            // 查出服务抵扣券信息
            List<CouponInfo> couponInfoListDB = couponInfoService.getListByIds(couponIds);

            // 筛选服务抵扣劵
            result = couponInfoService.getAvailableCoupon(couponInfoListDB, availableCouponForm.getRealRecyclerAmount());
        }
        return result;
    }

    /**
     * 回收员使用服务抵扣劵
     * @param useCouponFrom
     * @return
     */
    @Transactional
    @Override
    public Boolean useCoupon(UseCouponFrom useCouponFrom) {
        LambdaUpdateWrapper<RecyclerCoupon> recyclerCouponLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        recyclerCouponLambdaUpdateWrapper.eq(RecyclerCoupon::getRecyclerId,useCouponFrom.getRecyclerId())
                .eq(RecyclerCoupon::getCouponId,useCouponFrom.getCouponId())
                .set(RecyclerCoupon::getUsedTime,useCouponFrom.getUsedTime())
                .set(RecyclerCoupon::getOrderId,useCouponFrom.getOrderId())
                .set(RecyclerCoupon::getStatus,useCouponFrom.getStatus());
        // 修改服务抵扣劵的使用数量
        couponInfoService.updateUseCount(useCouponFrom.getCouponId());
        return this.update(recyclerCouponLambdaUpdateWrapper);
    }

    /**
     * 获取回收员的服务抵扣劵
     * @param recyclerId
     * @return
     */
    @Override
    public List<CouponNotUsedVo> getNotUsedCoupon(Long recyclerId) {
        LambdaQueryWrapper<RecyclerCoupon> recyclerCouponLambdaQueryWrapper = new LambdaQueryWrapper<>();
        recyclerCouponLambdaQueryWrapper.eq(RecyclerCoupon::getRecyclerId,recyclerId)
                .eq(RecyclerCoupon::getStatus,CouponConstant.UNUSED_STATUS);
        List<RecyclerCoupon> recyclerCouponListDB = this.list(recyclerCouponLambdaQueryWrapper);
        if (ObjectUtils.isEmpty(recyclerCouponListDB)) {
            return Collections.emptyList();
        }
        return recyclerCouponListDB.stream().map(item -> {
            CouponInfo couponInfoDB = couponInfoService.getById(item.getCouponId());
            if (ObjectUtils.isEmpty(couponInfoDB)) {
                return null;
            }
            CouponNotUsedVo couponNotUsedVo = new CouponNotUsedVo();
            BeanUtils.copyProperties(couponInfoDB,couponNotUsedVo);
            couponNotUsedVo.setCouponId(couponInfoDB.getId());
            couponNotUsedVo.setExpireTime(item.getExpireTime());
            return couponNotUsedVo;
        }).filter(Objects::nonNull).collect(Collectors.toList());
    }
}
