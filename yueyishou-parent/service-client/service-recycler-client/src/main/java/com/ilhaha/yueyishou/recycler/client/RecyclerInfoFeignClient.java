package com.ilhaha.yueyishou.recycler.client;

import org.springframework.cloud.openfeign.FeignClient;

/**
 * @Author ilhaha
 * @Create 2024/9/2 22:46
 * @Version 1.0
 */
@FeignClient("service-recycler")
public interface RecyclerInfoFeignClient {
}
