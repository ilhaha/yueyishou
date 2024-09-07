package com.ilhaha.yueyishou.customer.client;

import com.ilhaha.yueyishou.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @Author ilhaha
 * @Create 2024/9/2 16:05
 * @Version 1.0
 */
@FeignClient("service-customer")
public interface CustomerInfoFeignClient {

    /**
     * 小程序授权登录
     * @param code
     * @return
     */
    @GetMapping("/customerInfo/login/{code}")
    Result<String> login(@PathVariable("code") String code);
}
