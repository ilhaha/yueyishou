package com.ilhaha.yueyishou.tencentcloud.service.impl;

import com.ilhaha.yueyishou.constant.PublicConstant;
import com.ilhaha.yueyishou.execption.YueYiShouException;
import com.ilhaha.yueyishou.result.ResultCodeEnum;
import com.ilhaha.yueyishou.tencentcloud.config.TencentCloudProperties;
import com.ilhaha.yueyishou.tencentcloud.service.CiService;
import com.ilhaha.yueyishou.tencentcloud.service.CosService;
import com.ilhaha.yueyishou.vo.recycler.CosUploadVo;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.http.HttpMethodName;
import com.qcloud.cos.model.GeneratePresignedUrlRequest;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.StorageClass;
import jakarta.annotation.Resource;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.UUID;

/**
 * @Author ilhaha
 * @Create 2024/9/8 15:21
 * @Version 1.0
 */
@Service
public class CosServiceImpl implements CosService {

    @Resource
    private COSClient cosClient;

    @Resource
    private CiService ciService;

    @Resource
    private TencentCloudProperties tencentCloudProperties;

    /**
     * 使用腾讯云cos上传图片
     * @param file
     * @param path
     * @return
     */
    @Override
    public CosUploadVo upload(MultipartFile file, String path) {
        //元数据信息
        ObjectMetadata meta = new ObjectMetadata();
        meta.setContentLength(file.getSize());
        meta.setContentEncoding("UTF-8");
        meta.setContentType(file.getContentType());

        //向存储桶中保存文件
        String fileType = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")); //文件后缀名
        String uploadPath = "/recycler/" + path + "/" + UUID.randomUUID().toString().replaceAll("-", "") + fileType;
        PutObjectRequest putObjectRequest = null;
        try {
            putObjectRequest = new PutObjectRequest(tencentCloudProperties.getBucketPrivate(), uploadPath, file.getInputStream(), meta);
        } catch (IOException e) {
            throw new YueYiShouException(ResultCodeEnum.IMAGE_UPLOAD_FAIL);
        }
        putObjectRequest.setStorageClass(StorageClass.Standard);
        cosClient.putObject(putObjectRequest); //上传文件
        //审核图片
        Boolean isAuditing = ciService.imageAuditing(uploadPath);
        if(!isAuditing) {
            //删除违规图片
            cosClient.deleteObject(tencentCloudProperties.getBucketPrivate(), uploadPath);
            throw new YueYiShouException(ResultCodeEnum.IMAGE_AUDITION_FAIL);
        }
        //封装返回对象
        CosUploadVo cosUploadVo = new CosUploadVo();
        cosUploadVo.setUrl(uploadPath);
        //图片临时访问url，回显使用
        cosUploadVo.setShowUrl(getImageUrl(uploadPath));
        return cosUploadVo;
    }

    /**
     * 获取图片临时签名URL
     * @param path
     * @return
     */
    @Override
    public String getImageUrl(String path) {
        if(!StringUtils.hasText(path)) return "";
        GeneratePresignedUrlRequest request =
                new GeneratePresignedUrlRequest(tencentCloudProperties.getBucketPrivate(), path, HttpMethodName.GET);
        //设置临时URL有效期为30分钟
        Date expiration = new DateTime().plusMinutes(PublicConstant.COS_IMAGE_VALID_TIME).toDate();
        request.setExpiration(expiration);
        URL url = cosClient.generatePresignedUrl(request);
        return url.toString();
    }
}
