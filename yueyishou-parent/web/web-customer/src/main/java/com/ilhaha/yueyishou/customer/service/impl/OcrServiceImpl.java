package com.ilhaha.yueyishou.customer.service.impl;

import com.ilhaha.yueyishou.common.result.Result;
import com.ilhaha.yueyishou.customer.service.OcrService;
import com.ilhaha.yueyishou.model.vo.tencentcloud.IdCardOcrVo;
import com.ilhaha.yueyishou.tencentcloud.client.OcrFeignClient;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author ilhaha
 * @Create 2024/9/24 22:14
 * @Version 1.0
 */
@Service
public class OcrServiceImpl implements OcrService {

    @Resource
    private OcrFeignClient ocrFeignClient;

    /**
     * 身份证识别
     * @param file
     * @return
     */
    @Override
    public Result<IdCardOcrVo> idCardOcr(MultipartFile file) {
        return ocrFeignClient.idCardOcr(file);
    }
}
