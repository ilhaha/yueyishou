package com.ilhaha.yueyishou.recycler.controller;

import com.ilhaha.yueyishou.recycler.service.RecyclerService;
import com.ilhaha.yueyishou.common.result.Result;
import com.ilhaha.yueyishou.model.vo.tencentcloud.CosUploadVo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author ilhaha
 * @Create 2024/9/8 17:18
 * @Version 1.0
 */
@RestController
@RequestMapping("/recycler")
public class RecyclerController {

    @Resource
    private RecyclerService recyclerService;

    /**
     * 回收员上传图片
     * @param file
     * @return
     */
    @PostMapping("/upload")
    public Result<CosUploadVo> upload(@RequestPart("file") MultipartFile file, @RequestParam(name = "path") String path){
        return recyclerService.upload(file,path);
    }
}
