package com.ilhaha.yueyishou.model.vo.recycler;

import lombok.Data;

/**
 * @Author ilhaha
 * @Create 2024/9/26 09:53
 * @Version 1.0
 */
@Data
public class RecyclerAuthImagesVo {
    /**身份证正面*/
    private String idcardFrontUrl;
    /**身份证背面*/
    private String idcardBackUrl;
    /**手持身份证*/
    private String idcardHandUrl;
    /**正脸照*/
    private String fullFaceUrl;
    /**人脸模型*/
    private String faceRecognitionUrl;
    /**认证状态*/
    private Integer authStatus;
}
