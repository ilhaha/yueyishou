package com.ilhaha.yueyishou.tencentcloud.controller;

import com.ilhaha.yueyishou.common.result.Result;
import com.ilhaha.yueyishou.model.vo.tencentcloud.IdCardOcrVo;
import com.ilhaha.yueyishou.tencentcloud.service.OcrService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author ilhaha
 * @Create 2024/9/24 21:56
 * @Version 1.0
 */
@RestController
@RequestMapping("/ocr")
public class OcrController {

    @Resource
    private OcrService ocrService;

    /**
     * 身份证识别
     * @param file
     * @return
     */
    @PostMapping("/idCardOcr")
    public Result<IdCardOcrVo> idCardOcr(@RequestPart("file") MultipartFile file) {
        return Result.ok(ocrService.idCardOcr(file));
    }
}
