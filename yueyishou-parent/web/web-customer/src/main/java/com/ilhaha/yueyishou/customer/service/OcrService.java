package com.ilhaha.yueyishou.customer.service;

import com.ilhaha.yueyishou.common.result.Result;
import com.ilhaha.yueyishou.model.vo.tencentcloud.IdCardOcrVo;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author ilhaha
 * @Create 2024/9/24 22:13
 * @Version 1.0
 */
public interface OcrService {
    /**
     * 身份证识别
     * @param file
     * @return
     */
    Result<IdCardOcrVo> idCardOcr(MultipartFile file);
}
