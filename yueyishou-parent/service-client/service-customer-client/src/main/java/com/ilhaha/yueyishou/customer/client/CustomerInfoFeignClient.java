package com.ilhaha.yueyishou.customer.client;

import org.springframework.cloud.openfeign.FeignClient;

/**
 * @Author ilhaha
 * @Create 2024/9/2 16:05
 * @Version 1.0
 */
@FeignClient("service-customer")
public interface CustomerInfoFeignClient {
}
