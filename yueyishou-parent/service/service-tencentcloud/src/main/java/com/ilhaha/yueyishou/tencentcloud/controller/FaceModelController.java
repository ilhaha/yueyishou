package com.ilhaha.yueyishou.tencentcloud.controller;

import com.ilhaha.yueyishou.common.result.Result;
import com.ilhaha.yueyishou.model.form.tencentcloud.FaceModelForm;
import com.ilhaha.yueyishou.model.vo.recycler.RecyclerFaceModelForm;
import com.ilhaha.yueyishou.model.vo.tencentcloud.CosUploadVo;
import com.ilhaha.yueyishou.model.vo.tencentcloud.FaceModelVo;
import com.ilhaha.yueyishou.tencentcloud.service.FaceModelService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author ilhaha
 * @Create 2024/9/25 13:43
 * @Version 1.0
 */
@RestController
@RequestMapping("/face/model")
public class FaceModelController {

    @Resource
    private FaceModelService faceModelService;

    /***
     * 回收员每日人脸识别
     * @param recyclerFaceModelForm
     * @return
     */
    @PostMapping("/verifyDriverFace")
    public Result<Boolean> verifyDriverFace(@RequestBody RecyclerFaceModelForm recyclerFaceModelForm) {
        return Result.ok(faceModelService.verifyDriverFace(recyclerFaceModelForm));
    }

    /**
     * 创建人脸模型
     * @param file
     * @param faceModelForm
     * @return
     */
    @PostMapping("/create/{customerId}")
    public Result<FaceModelVo> createFaceModel(@RequestPart("file") MultipartFile file,
                                               @RequestPart("faceModelForm") String faceModelForm,
                                               @PathVariable("customerId") Long customerId) {
        return Result.ok(faceModelService.createFaceModel(file, faceModelForm,customerId));
    }


    /**
     * 删除人脸模型
     * @param customerId
     * @return
     */
    @PostMapping("/remove/{customerId}")
    public Result<Boolean> removeFaceModel(@PathVariable("customerId") Long customerId) {
        return Result.ok(faceModelService.deleteFaceModel(customerId));
    }


}
