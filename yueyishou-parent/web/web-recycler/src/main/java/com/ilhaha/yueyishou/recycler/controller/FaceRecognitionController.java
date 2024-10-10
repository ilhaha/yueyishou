package com.ilhaha.yueyishou.recycler.controller;

import com.ilhaha.yueyishou.common.anno.LoginVerification;
import com.ilhaha.yueyishou.common.result.Result;
import com.ilhaha.yueyishou.model.vo.recycler.RecyclerFaceModelForm;
import com.ilhaha.yueyishou.recycler.service.FaceRecognitionService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

/**
 * @Author ilhaha
 * @Create 2024/10/10 15:52
 * @Version 1.0
 */
@RestController
@RequestMapping("/faceRecognition")
public class FaceRecognitionController {

    @Resource
    private FaceRecognitionService faceRecognitionService;

    /***
     * 回收员每日人脸识别
     * @param recyclerFaceModelForm
     * @return
     */
    @LoginVerification
    @PostMapping("/verifyDriverFace")
    public Result<Boolean> verifyDriverFace(@RequestBody RecyclerFaceModelForm recyclerFaceModelForm) {
        return faceRecognitionService.verifyDriverFace(recyclerFaceModelForm);
    }


    /**
     * 判读回收员是否已进行了每日人脸签到
     * @return
     */
    @LoginVerification
    @GetMapping("/is/sign")
    public Result<Boolean> isSign(){
        return faceRecognitionService.isSign();
    }
}
