package com.ilhaha.yueyishou.tencentcloud.client;

import com.ilhaha.yueyishou.common.result.Result;
import com.ilhaha.yueyishou.model.vo.recycler.RecyclerFaceModelForm;
import com.ilhaha.yueyishou.model.vo.tencentcloud.FaceModelVo;
import lombok.extern.java.Log;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@FeignClient("service-tencentcloud")
public interface FaceModelFeignClient {


    /***
     * 回收员每日人脸识别
     * @param recyclerFaceModelForm
     * @return
     */
    @PostMapping("/face/model/verifyDriverFace")
    Result<Boolean> verifyDriverFace(@RequestBody RecyclerFaceModelForm recyclerFaceModelForm);


    /**
     * 创建人脸识别模型
     * @param file 文件
     * @param faceModelForm 表单参数
     * @return 结果
     */
    @PostMapping(value = "/face/model/create/{customerId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    Result<FaceModelVo> createFaceModel(@RequestPart("file") MultipartFile file,
                                        @RequestPart("faceModelForm") String faceModelForm,
                                        @PathVariable("customerId") Long customerId);


    /**
     * 删除人脸模型
     * @param customerId
     * @return
     */
    @PostMapping("/face/model/remove/{customerId}")
    Result<Boolean> removeFaceModel(@PathVariable("customerId") Long customerId);

}

