package com.ilhaha.yueyishou.coupon.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ilhaha.yueyishou.common.execption.YueYiShouException;
import com.ilhaha.yueyishou.common.result.ResultCodeEnum;
import com.ilhaha.yueyishou.coupon.mapper.CouponInfoMapper;
import com.ilhaha.yueyishou.coupon.service.ICouponInfoService;
import com.ilhaha.yueyishou.model.constant.CouponConstant;
import com.ilhaha.yueyishou.model.entity.coupon.CouponInfo;
import com.ilhaha.yueyishou.model.vo.coupon.AvailableCouponVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CouponInfoServiceImpl extends ServiceImpl<CouponInfoMapper, CouponInfo> implements ICouponInfoService {

    /**
     * 通过id集合获取服务抵扣劵集合
     *
     * @param couponIds
     * @return
     */
    @Override
    public List<CouponInfo> getListByIds(List<Long> couponIds) {
        LambdaQueryWrapper<CouponInfo> couponInfoLambdaQueryWrapper = new LambdaQueryWrapper<>();
        couponInfoLambdaQueryWrapper.in(CouponInfo::getId, couponIds);
        return this.list(couponInfoLambdaQueryWrapper);
    }

    /**
     * 更新服务抵扣劵领取数量
     *
     * @param couponIds
     */
    @Override
    public void updateCouponReceiveQuantity(List<Long> couponIds) {
        LambdaUpdateWrapper<CouponInfo> couponInfoLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        couponInfoLambdaUpdateWrapper.setSql("receive_count = receive_count + 1")
                .in(CouponInfo::getId, couponIds);
        this.update(couponInfoLambdaUpdateWrapper);
    }

    /**
     * 获取可使用的服务抵扣劵
     *
     * @param couponInfoList
     * @param realRecyclerAmount
     * @return
     */
    @Override
    public List<AvailableCouponVo> getAvailableCoupon(List<CouponInfo> couponInfoList, BigDecimal realRecyclerAmount) {
        List<AvailableCouponVo> result = new ArrayList<>();

        // 过滤出满足门槛的优惠券
        List<CouponInfo> meetThresholdList = couponInfoList.stream()
                .filter(item ->
                        BigDecimal.valueOf(CouponConstant.NO_THRESHOLD).compareTo(item.getConditionAmount()) == 0 ||
                                realRecyclerAmount.compareTo(item.getConditionAmount()) >= 0
                )
                .collect(Collectors.toList());

        // 排序逻辑：1. 优先免单券 2. 优惠金额最多的券
        meetThresholdList.sort((o1, o2) -> {
            // 如果两张券都是免单券，按门槛金额升序排序
            if (CouponConstant.FREE_CHARGE_TYPE.equals(o1.getCouponType()) && CouponConstant.FREE_CHARGE_TYPE.equals(o2.getCouponType())) {
                return o1.getConditionAmount().compareTo(o2.getConditionAmount());
            }
            // 如果只有一张券是免单券，则免单券优先
            if (CouponConstant.FREE_CHARGE_TYPE.equals(o1.getCouponType())) return -1;
            if (CouponConstant.FREE_CHARGE_TYPE.equals(o2.getCouponType())) return 1;

            // 计算折扣后的金额：realRecyclerAmount * (discount / 10)
            BigDecimal discount1 = realRecyclerAmount.multiply(o1.getDiscount().divide(BigDecimal.TEN));
            BigDecimal discount2 = realRecyclerAmount.multiply(o2.getDiscount().divide(BigDecimal.TEN));

            // 按照优惠后的金额降序排序（折扣越大，支付越少）
            return discount2.compareTo(discount1);
        });

        // 将排序后的 CouponInfo 转换为 AvailableCouponVo
        result = meetThresholdList.stream().map(item -> {
            AvailableCouponVo availableCouponVo = new AvailableCouponVo();
            BeanUtils.copyProperties(item, availableCouponVo);
            return availableCouponVo;
        }).collect(Collectors.toList());

        return result;
    }

    /**
     * 修改服务抵扣劵的使用数量
     * @param couponId
     * @return
     */
    @Override
    public Boolean updateUseCount(Long couponId) {
        LambdaUpdateWrapper<CouponInfo> couponInfoLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        couponInfoLambdaUpdateWrapper.eq(CouponInfo::getId,couponId)
                .setSql("use_count = use_count + 1")
                .set(CouponInfo::getUpdateTime,new Date());
        return this.update(couponInfoLambdaUpdateWrapper);
    }
}
