package com.ilhaha.yueyishou.coupon.client;


import com.ilhaha.yueyishou.common.result.Result;
import com.ilhaha.yueyishou.model.form.coupon.FreeIssueForm;
import org.springframework.cloud.openfeign.FeignClient;
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
     *  免费发放服务抵扣劵
     * @param freeIssueFormList
     * @param recyclerCount
     * @return
     */
    @PostMapping("/recyclerCoupon/free/issue/{recyclerCount}")
    Result<Boolean> freeIssue(@RequestBody List<FreeIssueForm> freeIssueFormList,
                              @PathVariable("recyclerCount") Integer recyclerCount);

}
