package com.ilhaha.yueyishou.coupon.client;


import com.ilhaha.yueyishou.common.result.Result;
import com.ilhaha.yueyishou.model.form.coupon.AvailableCouponForm;
import com.ilhaha.yueyishou.model.form.coupon.FreeIssueForm;
import com.ilhaha.yueyishou.model.form.coupon.UseCouponFrom;
import com.ilhaha.yueyishou.model.vo.coupon.AvailableCouponVo;
import com.ilhaha.yueyishou.model.vo.coupon.CouponNotUsedVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @Author ilhaha
 * @Create 2024/9/6 08:06
 * @Version 1.0
 */
@FeignClient("service-coupon")
public interface CustomerCouponFeignClient {

    /**
     * 获取顾客的服务抵扣劵
     * @param customerId
     * @return
     */
    @GetMapping("/customerCoupon/not/used/{customerId}")
    Result<List<CouponNotUsedVo>> getNotUsedCoupon(@PathVariable("customerId") Long customerId);

    /**
     * 顾客使用服务抵扣劵
     * @param useCouponFrom
     * @return
     */
    @PostMapping("/customerCoupon/use")
    Result<Boolean> useCoupon(@RequestBody UseCouponFrom useCouponFrom);

    /**
     *  免费发放服务抵扣劵
     * @param freeIssueFormList
     * @return
     */
    @PostMapping("/customerCoupon/free/issue")
    Result<Boolean> freeIssue(@RequestBody List<FreeIssueForm> freeIssueFormList);

    /**
     * 获取顾客在订单中可使用的服务抵扣劵
     * @param availableCouponForm
     * @return
     */
    @PostMapping("/customerCoupon/available")
    Result<List<AvailableCouponVo>> getAvailableCustomerServiceCoupons(@RequestBody AvailableCouponForm availableCouponForm);

}
