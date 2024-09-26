package com.ilhaha.yueyishou.tencentcloud.service.impl;

import com.alibaba.nacos.common.codec.Base64;
import com.ilhaha.yueyishou.common.execption.YueYiShouException;
import com.ilhaha.yueyishou.common.result.ResultCodeEnum;
import com.ilhaha.yueyishou.model.constant.TencentcloudConstant;
import com.ilhaha.yueyishou.model.enums.Gender;
import com.ilhaha.yueyishou.model.vo.tencentcloud.CosUploadVo;
import com.ilhaha.yueyishou.model.vo.tencentcloud.IdCardOcrVo;
import com.ilhaha.yueyishou.tencentcloud.service.CosService;
import com.ilhaha.yueyishou.tencentcloud.service.OcrService;
import com.tencentcloudapi.ocr.v20181119.OcrClient;
import com.tencentcloudapi.ocr.v20181119.models.IDCardOCRRequest;
import com.tencentcloudapi.ocr.v20181119.models.IDCardOCRResponse;
import jakarta.annotation.Resource;
import org.joda.time.format.DateTimeFormat;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author ilhaha
 * @Create 2024/9/24 22:02
 * @Version 1.0
 */
@Service
public class OcrServiceImpl implements OcrService {

    @Resource
    private CosService cosService;

    @Resource
    private OcrClient ocrClient;

    /**
     * 识别身份证
     * @param file
     * @return
     */
    @Override
    public IdCardOcrVo idCardOcr(MultipartFile file) {
        try {
            byte[] encoder = Base64.encodeBase64(file.getBytes());
            String idCardBase64 = new String(encoder);
            // 实例化一个请求对象,每个接口都会对应一个request对象
            IDCardOCRRequest req = new IDCardOCRRequest();
            req.setImageBase64(idCardBase64);
            // 返回的resp是一个IDCardOCRResponse的实例，与请求对象对应
            IDCardOCRResponse resp = ocrClient.IDCardOCR(req);
            //转换为IdCardOcrVo对象
            IdCardOcrVo idCardOcrVo = new IdCardOcrVo();
            if (StringUtils.hasText(resp.getName())) {
                //身份证正面
                idCardOcrVo.setName(resp.getName());
                idCardOcrVo.setGender(Gender.MALE.getComment().equals(resp.getSex()) ? String.valueOf(Gender.MALE.getValue()) : String.valueOf(Gender.WOMAN.getValue()));
                idCardOcrVo.setBirthday(DateTimeFormat.forPattern("yyyy/MM/dd").parseDateTime(resp.getBirth()).toString("yyyy-MM-dd"));
                idCardOcrVo.setIdcardNo(resp.getIdNum());
                idCardOcrVo.setIdcardAddress(resp.getAddress());

                //上传身份证正面图片到腾讯云cos
                CosUploadVo cosUploadVo = cosService.upload(file, TencentcloudConstant.IDCARD_DEFAULT_URL);
                idCardOcrVo.setIdcardFrontUrl(cosUploadVo.getUrl());
            } else {
                //身份证反面
                //证件有效期："2010.07.21-2020.07.21"
                String idcardExpireString = resp.getValidDate().split("-")[1];
                idCardOcrVo.setIdcardExpire(idcardExpireString.replace(".","/"));
                //上传身份证反面图片到腾讯云cos
                CosUploadVo cosUploadVo = cosService.upload(file, TencentcloudConstant.IDCARD_DEFAULT_URL);
                idCardOcrVo.setIdcardBackUrl(cosUploadVo.getUrl());
            }
            return idCardOcrVo;
        } catch (Exception e) {
            throw new YueYiShouException(ResultCodeEnum.ID_CARD_NOT_RECOGNIZED);
        }
    }
}
