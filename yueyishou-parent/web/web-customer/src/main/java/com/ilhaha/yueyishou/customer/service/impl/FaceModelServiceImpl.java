package com.ilhaha.yueyishou.customer.service.impl;

import com.ilhaha.yueyishou.common.result.Result;
import com.ilhaha.yueyishou.common.util.AuthContextHolder;
import com.ilhaha.yueyishou.customer.service.FaceModelService;
import com.ilhaha.yueyishou.model.vo.tencentcloud.FaceModelVo;
import com.ilhaha.yueyishou.tencentcloud.client.FaceModelFeignClient;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author ilhaha
 * @Create 2024/9/25 14:51
 * @Version 1.0
 */
@Service
public class FaceModelServiceImpl implements FaceModelService {

    @Resource
    private FaceModelFeignClient faceModelFeignClient;

    /**
     * 创建人脸识别
     * @param file
     * @param faceModelForm
     * @return
     */
    @Override
    public Result<FaceModelVo> createFaceModel(MultipartFile file, String faceModelForm) {
        return faceModelFeignClient.createFaceModel(file,faceModelForm, AuthContextHolder.getCustomerId());
    }

    /**
     * 删除人脸模型
     * @return
     */
    @Override
    public Result<Boolean> deleteFaceModel() {
        return faceModelFeignClient.removeFaceModel(AuthContextHolder.getCustomerId());
    }


}
