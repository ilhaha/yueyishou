package com.ilhaha.yueyishou.tencentcloud.service.impl;

import com.ilhaha.yueyishou.tencentcloud.config.TencentCloudProperties;
import com.ilhaha.yueyishou.tencentcloud.service.CiService;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.model.ciModel.auditing.ImageAuditingRequest;
import com.qcloud.cos.model.ciModel.auditing.ImageAuditingResponse;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * @Author ilhaha
 * @Create 2024/9/8 15:36
 * @Version 1.0
 */
@Service
public class CiServiceImpl implements CiService {

    @Resource
    private COSClient cosClient;

    @Resource
    private TencentCloudProperties tencentCloudProperties;

    /**
     * 图片审核
     * @param path
     * @return
     */
    @Override
    public Boolean imageAuditing(String path) {
        //审核图片内容
        //1.创建任务请求对象
        ImageAuditingRequest request = new ImageAuditingRequest();
        //2.添加请求参数 参数详情请见 API 接口文档
        //2.1设置请求 bucket
        request.setBucketName(tencentCloudProperties.getBucketPrivate());
        //2.2设置审核策略 不传则为默认策略（预设）
        //request.setBizType("");
        //2.3设置 bucket 中的图片位置
        request.setObjectKey(path);
        //3.调用接口,获取任务响应对象
        ImageAuditingResponse response = cosClient.imageAuditing(request);
        cosClient.shutdown();
        //用于返回该审核场景的审核结果，返回值：0：正常。1：确认为当前场景的违规内容。2：疑似为当前场景的违规内容。
        if (!response.getPornInfo().getHitFlag().equals("0")
                || !response.getAdsInfo().getHitFlag().equals("0")
                || !response.getTerroristInfo().getHitFlag().equals("0")
                || !response.getPoliticsInfo().getHitFlag().equals("0")
        ) {
            return false;
        }
        return true;
    }
}
