package com.ilhaha.yueyishou.recycler.controller;

import com.ilhaha.yueyishou.common.anno.LoginVerification;
import com.ilhaha.yueyishou.common.result.Result;
import com.ilhaha.yueyishou.model.vo.coupon.CouponNotUsedVo;
import com.ilhaha.yueyishou.recycler.service.CouponService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author ilhaha
 * @Create 2024/10/30 14:44
 * @Version 1.0
 */
@RestController
@RequestMapping("/coupon")
public class CouponController {

    @Resource
    private CouponService couponService;

    /**
     * 获取回收员的服务抵扣劵
     * @return
     */
    @LoginVerification
    @GetMapping("/not/used")
    public Result<List<CouponNotUsedVo>> getNotUsedCoupon(){
        return couponService.getNotUsedCoupon();
    }

}
