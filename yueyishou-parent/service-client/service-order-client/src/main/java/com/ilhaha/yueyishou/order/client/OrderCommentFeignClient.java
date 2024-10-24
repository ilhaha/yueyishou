package com.ilhaha.yueyishou.order.client;

import com.ilhaha.yueyishou.common.result.Result;
import com.ilhaha.yueyishou.model.form.order.ReviewForm;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Author ilhaha
 * @Create 2024/10/24 14:55
 * @Version 1.0
 */
@FeignClient("service-order")
public interface OrderCommentFeignClient {

    /**
     * 顾客评论
     * @return
     */
    @PostMapping("/orderComment/review")
    Result<Boolean> review(@RequestBody ReviewForm reviewForm);
}
