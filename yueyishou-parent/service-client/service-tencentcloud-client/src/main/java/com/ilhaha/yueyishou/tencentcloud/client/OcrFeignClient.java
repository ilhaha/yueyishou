package com.ilhaha.yueyishou.tencentcloud.client;

import com.ilhaha.yueyishou.common.result.Result;
import com.ilhaha.yueyishou.model.vo.tencentcloud.IdCardOcrVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author ilhaha
 * @Create 2024/9/24 22:12
 * @Version 1.0
 */
@FeignClient("service-tencentcloud")
public interface OcrFeignClient {
    /**
     * 身份证识别
     * @param file
     * @return
     */
    @PostMapping(value = "/ocr/idCardOcr",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    Result<IdCardOcrVo> idCardOcr(@RequestPart("file") MultipartFile file);
}
