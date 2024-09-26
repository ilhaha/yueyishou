package com.ilhaha.yueyishou.customer.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ilhaha.yueyishou.common.anno.LoginVerification;
import com.ilhaha.yueyishou.common.result.Result;
import com.ilhaha.yueyishou.customer.service.FaceModelService;
import com.ilhaha.yueyishou.model.form.tencentcloud.FaceModelForm;
import com.ilhaha.yueyishou.model.vo.tencentcloud.FaceModelVo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author ilhaha
 * @Create 2024/9/25 14:49
 * @Version 1.0
 */
@RestController
@RequestMapping("/faceModel")
public class FaceModelController {


    @Resource
    private FaceModelService faceModelService;

    /**
     * 创建人脸识别
     * @param file
     * @param faceModelForm
     * @return
     */
    @LoginVerification
    @PostMapping("/create")
    public Result<FaceModelVo> createFaceModel(@RequestPart("file") MultipartFile file,
                                               @RequestPart("faceModelForm") String faceModelForm){
        return faceModelService.createFaceModel(file,faceModelForm);
    }

    /**
     * 删除人脸模型
     * @return
     */
    @LoginVerification
    @PostMapping("/remove")
    public Result<Boolean> removeFaceModel() {
        return faceModelService.deleteFaceModel();
    }
}
