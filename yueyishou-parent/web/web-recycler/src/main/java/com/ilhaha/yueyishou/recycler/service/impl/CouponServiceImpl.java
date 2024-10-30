package com.ilhaha.yueyishou.recycler.service.impl;

import com.ilhaha.yueyishou.common.result.Result;
import com.ilhaha.yueyishou.common.util.AuthContextHolder;
import com.ilhaha.yueyishou.coupon.client.RecyclerCouponFeignClient;
import com.ilhaha.yueyishou.model.vo.coupon.CouponNotUsedVo;
import com.ilhaha.yueyishou.recycler.service.CouponService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author ilhaha
 * @Create 2024/10/30 14:46
 * @Version 1.0
 */
@Service
public class CouponServiceImpl implements CouponService {

    @Resource
    private RecyclerCouponFeignClient recyclerCouponFeignClient;

    /**
     * 获取回收员的服务抵扣劵
     * @return
     */
    @Override
    public Result<List<CouponNotUsedVo>> getNotUsedCoupon() {
        return recyclerCouponFeignClient.getNotUsedCoupon(AuthContextHolder.getRecyclerId());
    }
}
