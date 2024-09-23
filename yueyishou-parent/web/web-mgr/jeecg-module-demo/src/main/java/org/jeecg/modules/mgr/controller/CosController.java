package org.jeecg.modules.mgr.controller;

import com.ilhaha.yueyishou.model.vo.cos.CosUploadVo;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.mgr.service.CosService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * @Author ilhaha
 * @Create 2024/9/8 20:06
 * @Version 1.0
 */
@RestController
@RequestMapping("/cos")
public class CosController {

    @Resource
    private CosService cosService;

    /**
     * 文件上传
     * @param file
     * @param path
     * @return
     */
    @PostMapping("/upload")
    public Result<CosUploadVo> upload(@RequestPart("file") MultipartFile file, @RequestParam("path") String path){
        return Result.OK(cosService.upload(file,path));
    }

    /**
     * 获取腾讯云图片临时访问路径
     * @return
     */
//    @GetMapping("/image/url")
//    public Result<String> getImageUrl(@RequestParam("path") String path){
//        return Result.OK(cosService.getImageUrl(path));
//    }
}
