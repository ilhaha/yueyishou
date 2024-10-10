package com.ilhaha.yueyishou.recycler.service;

import com.ilhaha.yueyishou.common.result.Result;
import com.ilhaha.yueyishou.model.vo.recycler.RecyclerFaceModelForm;

/**
 * @Author ilhaha
 * @Create 2024/10/10 15:53
 * @Version 1.0
 */
public interface FaceRecognitionService {
    /**
     * 判读回收员是否已进行了每日人脸签到
     * @return
     */
    Result<Boolean> isSign();

    /***
     * 回收员每日人脸识别
     * @param recyclerFaceModelForm
     * @return
     */
    Result<Boolean> verifyDriverFace(RecyclerFaceModelForm recyclerFaceModelForm);
}
