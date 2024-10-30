package com.ilhaha.yueyishou.coupon.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ilhaha.yueyishou.coupon.mapper.CustomerCouponMapper;
import com.ilhaha.yueyishou.coupon.service.ICouponInfoService;
import com.ilhaha.yueyishou.coupon.service.ICustomerCouponService;
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
import io.seata.spring.annotation.GlobalTransactional;
import jakarta.annotation.Resource;
import org.checkerframework.checker.units.qual.C;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CustomerCouponServiceImpl extends ServiceImpl<CustomerCouponMapper, CustomerCoupon> implements ICustomerCouponService {

    @Resource
    private ICouponInfoService couponInfoService;

    /**
     * 免费发放服务抵扣劵
     *
     * @param freeIssueFormList
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean freeIssue(List<FreeIssueForm> freeIssueFormList) {
        List<CustomerCoupon> insetCustomerCouponList = freeIssueFormList.stream().map(item -> {
            CustomerCoupon customerCoupon = new CustomerCoupon();
            BeanUtils.copyProperties(item, customerCoupon);
            return customerCoupon;
        }).collect(Collectors.toList());
        // 更新优惠价的领取数量
        couponInfoService.updateCouponReceiveQuantity(freeIssueFormList.stream().map(item -> {
            return item.getCouponId();
        }).collect(Collectors.toList()));
        return this.saveBatch(insetCustomerCouponList);
    }

    /**
     * 获取顾客在订单中可使用的服务抵扣劵
     *
     * @param availableCouponForm
     * @return
     */
    @Override
    public List<AvailableCouponVo> getAvailableCustomerServiceCoupons(AvailableCouponForm availableCouponForm) {
        List<AvailableCouponVo> result = new ArrayList<>();
        // 查出顾客所有未使用的服务抵扣券
        LambdaQueryWrapper<CustomerCoupon> customerCouponLambdaQueryWrapper = new LambdaQueryWrapper<>();
        customerCouponLambdaQueryWrapper.eq(CustomerCoupon::getStatus, CouponConstant.UNUSED_STATUS)
                .eq(CustomerCoupon::getCustomerId, availableCouponForm.getCustomerId())
                .and(wrapper ->
                        wrapper.isNull(CustomerCoupon::getExpireTime)
                                .or()
                                .gt(CustomerCoupon::getExpireTime, LocalDateTime.now()));
        List<CustomerCoupon> customerCouponListDB = this.list(customerCouponLambdaQueryWrapper);
        if (!ObjectUtils.isEmpty(customerCouponListDB)) {
            List<Long> couponIds = customerCouponListDB.stream()
                    .map(CustomerCoupon::getCouponId)
                    .collect(Collectors.toList());

            // 查出服务抵扣券信息
            List<CouponInfo> couponInfoListDB = couponInfoService.getListByIds(couponIds);

            // 筛选服务抵扣劵
            result = couponInfoService.getAvailableCoupon(couponInfoListDB, availableCouponForm.getRealRecyclerAmount());
        }
        return result;
    }

    /**
     * 顾客使用服务抵扣劵
     * @param useCouponFrom
     * @return
     */
    @Transactional
    @Override
    public Boolean useCoupon(UseCouponFrom useCouponFrom) {
        LambdaUpdateWrapper<CustomerCoupon> customerCouponLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        customerCouponLambdaUpdateWrapper.eq(CustomerCoupon::getCustomerId,useCouponFrom.getCustomerId())
                .eq(CustomerCoupon::getCouponId,useCouponFrom.getCouponId())
                .set(CustomerCoupon::getUsedTime,useCouponFrom.getUsedTime())
                .set(CustomerCoupon::getOrderId,useCouponFrom.getOrderId())
                .set(CustomerCoupon::getStatus,useCouponFrom.getStatus());
        // 修改服务抵扣劵的使用数量
        couponInfoService.updateUseCount(useCouponFrom.getCouponId());
        return this.update(customerCouponLambdaUpdateWrapper);
    }

    /**
     * 获取顾客的服务抵扣劵
     * @param customerId
     * @return
     */
    @Override
    public List<CouponNotUsedVo> getNotUsedCoupon(Long customerId) {
        LambdaQueryWrapper<CustomerCoupon> customerCouponLambdaQueryWrapper = new LambdaQueryWrapper<>();
        customerCouponLambdaQueryWrapper.eq(CustomerCoupon::getCustomerId,customerId)
                .eq(CustomerCoupon::getStatus,CouponConstant.UNUSED_STATUS);
        List<CustomerCoupon> customerCouponListDB = this.list(customerCouponLambdaQueryWrapper);
        if (ObjectUtils.isEmpty(customerCouponListDB)) {
            return Collections.emptyList();
        }
        return customerCouponListDB.stream().map(item -> {
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
