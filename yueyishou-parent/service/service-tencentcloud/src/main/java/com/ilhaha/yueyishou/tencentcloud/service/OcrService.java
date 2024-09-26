package com.ilhaha.yueyishou.tencentcloud.service;

import com.ilhaha.yueyishou.model.vo.tencentcloud.IdCardOcrVo;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author ilhaha
 * @Create 2024/9/24 21:56
 * @Version 1.0
 */
public interface OcrService {

    /**
     * 识别身份证
     * @param file
     * @return
     */
    IdCardOcrVo idCardOcr(MultipartFile file);
}
