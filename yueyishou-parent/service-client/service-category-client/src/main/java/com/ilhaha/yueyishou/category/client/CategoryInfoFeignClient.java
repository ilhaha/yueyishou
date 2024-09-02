package com.ilhaha.yueyishou.category.client;

import org.springframework.cloud.openfeign.FeignClient;

/**
 * @Author ilhaha
 * @Create 2024/9/2 16:04
 * @Version 1.0
 */
@FeignClient("service-category")
public interface CategoryInfoFeignClient {
}
