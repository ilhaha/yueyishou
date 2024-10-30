package com.ilhaha.yueyishou.customer.service.impl;

import com.ilhaha.yueyishou.common.result.Result;
import com.ilhaha.yueyishou.common.util.AuthContextHolder;
import com.ilhaha.yueyishou.coupon.client.CustomerCouponFeignClient;
import com.ilhaha.yueyishou.customer.service.CouponService;
import com.ilhaha.yueyishou.model.vo.coupon.CouponNotUsedVo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author ilhaha
 * @Create 2024/10/29 23:05
 * @Version 1.0
 */
@Service
public class CouponServiceImpl implements CouponService {

    @Resource
    private CustomerCouponFeignClient customerCouponFeignClient;

    /**
     * 获取顾客的服务抵扣劵
     * @return
     */
    @Override
    public Result<List<CouponNotUsedVo>> getNotUsedCoupon() {
        return customerCouponFeignClient.getNotUsedCoupon(AuthContextHolder.getCustomerId());
    }
}
