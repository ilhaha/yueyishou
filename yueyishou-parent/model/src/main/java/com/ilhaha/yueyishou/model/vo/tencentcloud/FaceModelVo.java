package com.ilhaha.yueyishou.model.vo.tencentcloud;

import lombok.Data;

/**
 * @Author ilhaha
 * @Create 2024/9/25 13:47
 * @Version 1.0
 */
@Data
public class FaceModelVo {

    /**
     * 人脸图片路径
     */
    private String faceRecognitionUrl;

    /**
     * 腾讯云人脸模型Id
     */
    private String faceModelId;
}
