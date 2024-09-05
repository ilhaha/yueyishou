package com.ilhaha.yueyishou.coupon.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ilhaha.yueyishou.coupon.mapper.CustomerCouponMapper;
import com.ilhaha.yueyishou.coupon.service.ICustomerCouponService;
import com.ilhaha.yueyishou.entity.coupon.CustomerCoupon;
import org.springframework.stereotype.Service;

@Service
public class CustomerCouponServiceImpl extends ServiceImpl<CustomerCouponMapper, CustomerCoupon> implements ICustomerCouponService {

}
