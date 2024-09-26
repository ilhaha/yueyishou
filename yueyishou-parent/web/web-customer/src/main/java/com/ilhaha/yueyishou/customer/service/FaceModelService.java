package com.ilhaha.yueyishou.customer.service;

import com.ilhaha.yueyishou.common.result.Result;
import com.ilhaha.yueyishou.model.form.tencentcloud.FaceModelForm;
import com.ilhaha.yueyishou.model.vo.tencentcloud.FaceModelVo;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author ilhaha
 * @Create 2024/9/25 14:51
 * @Version 1.0
 */
public interface FaceModelService {
    /**
     * 创建人脸识别
     * @param file
     * @param faceModelForm
     * @return
     */
    Result<FaceModelVo> createFaceModel(MultipartFile file, String faceModelForm);

    /**
     * 删除人脸模型
     * @return
     */
    Result<Boolean> deleteFaceModel();


}
