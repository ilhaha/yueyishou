package com.ilhaha.yueyishou.recycler.service.impl;

import com.ilhaha.yueyishou.common.result.Result;
import com.ilhaha.yueyishou.common.util.AuthContextHolder;
import com.ilhaha.yueyishou.model.vo.recycler.RecyclerFaceModelForm;
import com.ilhaha.yueyishou.recycler.client.RecyclerFaceRecognitionFeignClient;
import com.ilhaha.yueyishou.recycler.service.FaceRecognitionService;
import com.ilhaha.yueyishou.tencentcloud.client.FaceModelFeignClient;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * @Author ilhaha
 * @Create 2024/10/10 15:53
 * @Version 1.0
 */
@Service
public class FaceRecognitionServiceImpl implements FaceRecognitionService {

    @Resource
    private RecyclerFaceRecognitionFeignClient recyclerFaceRecognitionFeignClient;

    @Resource
    private FaceModelFeignClient faceModelFeignClient;

    /**
     * 判读回收员是否已进行了每日人脸签到
     * @return
     */
    @Override
    public Result<Boolean> isSign() {
        return recyclerFaceRecognitionFeignClient.isSign(AuthContextHolder.getRecyclerId());
    }

    /***
     * 回收员每日人脸识别
     * @param recyclerFaceModelForm
     * @return
     */
    @Override
    public Result<Boolean> verifyDriverFace(RecyclerFaceModelForm recyclerFaceModelForm) {
        recyclerFaceModelForm.setCustomerId(AuthContextHolder.getCustomerId());
        recyclerFaceModelForm.setRecyclerId(AuthContextHolder.getRecyclerId());
        return faceModelFeignClient.verifyDriverFace(recyclerFaceModelForm);
    }
}
