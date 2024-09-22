package com.ilhaha.yueyishou.tencentcloud.controller;

import com.ilhaha.yueyishou.common.result.Result;
import com.ilhaha.yueyishou.tencentcloud.service.CosService;
import com.ilhaha.yueyishou.model.vo.cos.CosUploadVo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author ilhaha
 * @Create 2024/9/8 15:18
 * @Version 1.0
 */
@RestController
@RequestMapping("/cos")
public class CosController {

    @Resource
    private CosService cosService;

    /**
     * 使用腾讯云cos上传图片
     * @param file
     * @param path
     * @return
     */
    @PostMapping("/upload")
    public Result<CosUploadVo> upload(@RequestPart("file") MultipartFile file, @RequestParam("path") String path) {
        return Result.ok(cosService.upload(file, path));
    }

    /**
     * 获取腾讯云图片临时访问路径
     * @param path
     * @return
     */
    @GetMapping("/image/url")
    public Result<String> getImageUrl(@RequestParam("path") String path){
        return Result.ok(cosService.getImageUrl(path));
    }
}
