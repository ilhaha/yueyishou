package com.ilhaha.yueyishou.recycler.client;

import com.ilhaha.yueyishou.common.result.Result;
import com.ilhaha.yueyishou.model.entity.recycler.RecyclerFaceRecognition;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @Author ilhaha
 * @Create 2024/9/2 20:12
 * @Version 1.0
 */
@FeignClient("service-recycler")

public interface RecyclerFaceRecognitionFeignClient {

    /**
     *   添加
     *
     * @param recyclerFaceRecognition
     * @return
     */
    @PostMapping(value = "/recyclerFaceRecognition/add")
    Result<String> add(@RequestBody RecyclerFaceRecognition recyclerFaceRecognition);

    /**
     * 判读回收员是否已进行了每日人脸签到
     * @param recyclerId
     * @return
     */
    @GetMapping("/recyclerFaceRecognition/is/sign/{recyclerId}")
    Result<Boolean> isSign(@PathVariable("recyclerId") Long recyclerId);
}
