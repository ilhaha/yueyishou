package com.ilhaha.yueyishou.coupon.client;


import com.ilhaha.yueyishou.common.result.Result;
import com.ilhaha.yueyishou.model.form.coupon.AvailableCouponForm;
import com.ilhaha.yueyishou.model.form.coupon.FreeIssueForm;
import com.ilhaha.yueyishou.model.form.coupon.UseCouponFrom;
import com.ilhaha.yueyishou.model.vo.coupon.AvailableCouponVo;
import com.ilhaha.yueyishou.model.vo.coupon.CouponNotUsedVo;
import com.ilhaha.yueyishou.model.vo.coupon.ExistingCouponVo;
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
public interface RecyclerCouponFeignClient {

    /**
     * 获取回收员已有的服务抵扣劵
     * @param recyclerIds
     * @return
     */
    @PostMapping("/recyclerCoupon/existing")
    Result<List<ExistingCouponVo>> getExistingCoupons(List<Long> recyclerIds);

    /**
     * 获取回收员的服务抵扣劵
     * @param recyclerId
     * @return
     */
    @GetMapping("/recyclerCoupon/not/used/{recyclerId}")
    Result<List<CouponNotUsedVo>> getNotUsedCoupon(@PathVariable("recyclerId") Long recyclerId);


    /**
     * 回收员使用服务抵扣劵
     * @param useCouponFrom
     * @return
     */
    @PostMapping("/recyclerCoupon/use")
    Result<Boolean> useCoupon(@RequestBody UseCouponFrom useCouponFrom);

    /**
     * 获取回收员在订单中可使用的服务抵扣劵
     * @param availableCouponForm
     * @return
     */
    @PostMapping("/recyclerCoupon/available")
    Result<List<AvailableCouponVo>> getAvailableCustomerServiceCoupons(@RequestBody AvailableCouponForm availableCouponForm);


    /**
     *  免费发放服务抵扣劵
     * @param freeIssueFormList
     * @param recyclerCount
     * @return
     */
    @PostMapping("/recyclerCoupon/free/issue/{recyclerCount}")
    Result<Boolean> freeIssue(@RequestBody List<FreeIssueForm> freeIssueFormList,
                              @PathVariable("recyclerCount") Integer recyclerCount);

}
