package com.ilhaha.yueyishou.tencentcloud.service;

import com.ilhaha.yueyishou.model.form.tencentcloud.FaceModelForm;
import com.ilhaha.yueyishou.model.vo.tencentcloud.FaceModelVo;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author ilhaha
 * @Create 2024/9/25 13:45
 * @Version 1.0
 */

public interface FaceModelService {

    /**
     * 创建人脸模型
     * @param file
     * @param faceModelForm
     * @return
     */
    FaceModelVo createFaceModel(MultipartFile file, String faceModelForm,Long customerId);

    /**
     * 删除人脸识别
     * @param customerId
     * @return
     */
    Boolean deleteFaceModel(Long customerId);
}
