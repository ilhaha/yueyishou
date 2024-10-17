package com.ilhaha.yueyishou.coupon.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ilhaha.yueyishou.coupon.mapper.CustomerCouponMapper;
import com.ilhaha.yueyishou.coupon.service.ICouponInfoService;
import com.ilhaha.yueyishou.coupon.service.ICustomerCouponService;
import com.ilhaha.yueyishou.coupon.service.IRecyclerCouponService;
import com.ilhaha.yueyishou.model.entity.coupon.CustomerCoupon;
import com.ilhaha.yueyishou.model.entity.coupon.RecyclerCoupon;
import com.ilhaha.yueyishou.model.form.coupon.FreeIssueForm;
import io.seata.spring.annotation.GlobalTransactional;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerCouponServiceImpl extends ServiceImpl<CustomerCouponMapper, CustomerCoupon> implements ICustomerCouponService {

    @Resource
    private ICouponInfoService couponInfoService;

    /**
     *  免费发放服务抵扣劵
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
        couponInfoService.updateCouponReceiveQuantity(freeIssueFormList.stream().map(item ->{
            return item.getCouponId();
        }).collect(Collectors.toList()));
        return this.saveBatch(insetCustomerCouponList);
    }
}
