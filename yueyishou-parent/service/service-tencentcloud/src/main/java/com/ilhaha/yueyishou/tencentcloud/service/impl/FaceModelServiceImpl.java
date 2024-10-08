package com.ilhaha.yueyishou.tencentcloud.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ilhaha.yueyishou.common.execption.YueYiShouException;
import com.ilhaha.yueyishou.common.result.Result;
import com.ilhaha.yueyishou.common.result.ResultCodeEnum;
import com.ilhaha.yueyishou.model.constant.TencentcloudConstant;
import com.ilhaha.yueyishou.model.entity.recycler.RecyclerFaceRecognition;
import com.ilhaha.yueyishou.model.entity.recycler.RecyclerInfo;
import com.ilhaha.yueyishou.model.form.tencentcloud.FaceModelForm;
import com.ilhaha.yueyishou.model.vo.recycler.RecyclerFaceModelForm;
import com.ilhaha.yueyishou.model.vo.tencentcloud.CosUploadVo;
import com.ilhaha.yueyishou.model.vo.tencentcloud.FaceModelVo;
import com.ilhaha.yueyishou.recycler.client.RecyclerFaceRecognitionFeignClient;
import com.ilhaha.yueyishou.recycler.client.RecyclerInfoFeignClient;
import com.ilhaha.yueyishou.tencentcloud.config.TencentCloudProperties;
import com.ilhaha.yueyishou.tencentcloud.service.CosService;
import com.ilhaha.yueyishou.tencentcloud.service.FaceModelService;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.iai.v20200303.IaiClient;
import com.tencentcloudapi.iai.v20200303.models.*;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.Date;

/**
 * @Author ilhaha
 * @Create 2024/9/25 13:45
 * @Version 1.0
 */
@Service
public class FaceModelServiceImpl implements FaceModelService {

    @Resource
    private TencentCloudProperties tencentCloudProperties;

    @Resource
    private CosService cosService;

    @Resource
    private IaiClient iaiClient;

    @Resource
    private RecyclerFaceRecognitionFeignClient recyclerFaceRecognitionFeignClient;

    /**
     * 创建人脸模型
     *
     * @param file
     * @param faceModelForm
     * @return
     */
    @Override
    public FaceModelVo createFaceModel(MultipartFile file, String faceModelForm, Long customerId) {
        FaceModelVo faceModelVo = new FaceModelVo();
        try {
            // 实例化一个请求对象,每个接口都会对应一个request对象
            CreatePersonRequest req = new CreatePersonRequest();
            req.setGroupId(tencentCloudProperties.getPersonGroupId());
            //基本信息
            FaceModelForm faceModelFormInfo = parseFormData(faceModelForm);
            faceModelFormInfo.setCustomerId(customerId);
            req.setPersonId(String.valueOf(faceModelFormInfo.getCustomerId()));
            req.setGender(Long.valueOf(faceModelFormInfo.getGender()));
            req.setQualityControl(4L);
            req.setUniquePersonControl(4L);
            req.setPersonName(faceModelFormInfo.getName());
            req.setImage(convertFileToBase64(file));

            // 返回的resp是一个CreatePersonResponse的实例，与请求对象对应
            CreatePersonResponse resp = iaiClient.CreatePerson(req);
            // 输出json格式的字符串回包
            if (StringUtils.hasText(resp.getFaceId())) {
                // 保存人脸模型图片
                CosUploadVo cosUploadVo = cosService.upload(file, TencentcloudConstant.FACE_MODEL_DEFAULT_URL);
                faceModelVo.setFaceModelId(resp.getFaceId());
                faceModelVo.setFaceRecognitionUrl(cosUploadVo.getUrl());
            }
        } catch (TencentCloudSDKException e) {
            throw new YueYiShouException(ResultCodeEnum.FACE_MODEL_CREATION_FAILED.getCode(), e.getMessage());
        }
        return faceModelVo;
    }

    /**
     * 删除人脸模型
     *
     * @param customerId
     * @return
     */
    @Override
    public Boolean deleteFaceModel(Long customerId) {
        try {
            // 实例化一个请求对象
            DeletePersonRequest req = new DeletePersonRequest();
            req.setPersonId(String.valueOf(customerId));

            // 调用删除人脸模型的 API
            iaiClient.DeletePerson(req);

        } catch (TencentCloudSDKException e) {
            e.printStackTrace();
        }
        return true;
    }

    /***
     * 回收员每日人脸识别
     * @param recyclerFaceModelForm
     * @return
     */
    @Override
    public Boolean verifyDriverFace(RecyclerFaceModelForm recyclerFaceModelForm) {
        try {
            // 实例化一个请求对象,每个接口都会对应一个request对象
            VerifyFaceRequest req = new VerifyFaceRequest();
            req.setImage(recyclerFaceModelForm.getImageBase64());
            req.setPersonId(String.valueOf(recyclerFaceModelForm.getCustomerId()));
            // 返回的resp是一个VerifyFaceResponse的实例，与请求对象对应
            VerifyFaceResponse resp = iaiClient.VerifyFace(req);
            if (resp.getIsMatch()) {
                //活体检查
                if(this.detectLiveFace(recyclerFaceModelForm.getImageBase64())) {
                    RecyclerFaceRecognition recyclerFaceRecognition = new RecyclerFaceRecognition();
                    recyclerFaceRecognition.setRecyclerId(recyclerFaceModelForm.getRecyclerId());
                    recyclerFaceRecognition.setFaceDate(new Date());
                    recyclerFaceRecognitionFeignClient.add(recyclerFaceRecognition);
                    return true;
                };
            }
        } catch (TencentCloudSDKException e) {
            e.printStackTrace();
        }
       return false;
    }

    /**
     * 人脸静态活体检测
     * @param imageBase64
     * @return
     */
    private Boolean detectLiveFace(String imageBase64) {
        try{
            // 实例化一个请求对象,每个接口都会对应一个request对象
            DetectLiveFaceRequest req = new DetectLiveFaceRequest();
            req.setImage(imageBase64);
            // 返回的resp是一个DetectLiveFaceResponse的实例，与请求对象对应
            DetectLiveFaceResponse resp = iaiClient.DetectLiveFace(req);
            if(resp.getIsLiveness()) {
                return true;
            }
        } catch (TencentCloudSDKException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 将 MultipartFile 转换为 Base64 编码的字符串
     *
     * @param file
     * @return
     */
    public static String convertFileToBase64(MultipartFile file) {
        // 获取 MultipartFile 的字节数组
        byte[] fileContent = new byte[0];
        try {
            fileContent = file.getBytes();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // 使用 Base64 对字节数组进行编码
        String base64Encoded = java.util.Base64.getEncoder().encodeToString(fileContent);
        return base64Encoded;
    }

    /**
     * 将JSON转成对应的FaceModelForm对象
     *
     * @param formData
     * @return
     */
    private FaceModelForm parseFormData(String formData) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(formData, FaceModelForm.class);
        } catch (Exception e) {
            throw new YueYiShouException(ResultCodeEnum.DATA_ERROR);
        }
    }
}
