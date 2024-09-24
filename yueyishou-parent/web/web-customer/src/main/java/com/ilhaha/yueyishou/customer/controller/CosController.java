package com.ilhaha.yueyishou.customer.controller;

import com.ilhaha.yueyishou.common.result.Result;
import com.ilhaha.yueyishou.customer.service.CosService;
import com.ilhaha.yueyishou.model.vo.cos.CosUploadVo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author ilhaha
 * @Create 2024/9/24 10:36
 * @Version 1.0
 */
@RestController
@RequestMapping("/cos")
public class CosController {

    @Resource
    private CosService cosService;

    /**
     * 上传文件
     * @param file
     * @param path
     * @return
     */
    @PostMapping("/upload")
    public Result<CosUploadVo> upload(@RequestPart("file") MultipartFile file,
                                      @RequestParam("path") String path){
        return cosService.upload(file,path);
    }
}
