package com.ilhaha.yueyishou.coupon.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ilhaha.yueyishou.common.execption.YueYiShouException;
import com.ilhaha.yueyishou.common.result.ResultCodeEnum;
import com.ilhaha.yueyishou.coupon.mapper.CouponInfoMapper;
import com.ilhaha.yueyishou.coupon.service.ICouponInfoService;
import com.ilhaha.yueyishou.model.entity.coupon.CouponInfo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CouponInfoServiceImpl extends ServiceImpl<CouponInfoMapper, CouponInfo> implements ICouponInfoService {

    /**
     * 通过id集合获取服务抵扣劵集合
     * @param couponIds
     * @return
     */
    @Override
    public List<CouponInfo> getListByIds(List<Long> couponIds) {
        LambdaQueryWrapper<CouponInfo> couponInfoLambdaQueryWrapper = new LambdaQueryWrapper<>();
        couponInfoLambdaQueryWrapper.in(CouponInfo::getId,couponIds);
        return this.list(couponInfoLambdaQueryWrapper);
    }

    /**
     * 更新服务抵扣劵领取数量
     * @param couponIds
     */
    @Override
    public void updateCouponReceiveQuantity(List<Long> couponIds) {
        LambdaUpdateWrapper<CouponInfo> couponInfoLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        couponInfoLambdaUpdateWrapper.setSql("receive_count = receive_count + 1")
                .in(CouponInfo::getId,couponIds);
        this.update(couponInfoLambdaUpdateWrapper);
    }
}
