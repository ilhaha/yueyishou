package com.ilhaha.yueyishou.recycler.service.impl;

import com.alibaba.fastjson2.util.DateUtils;
import com.ilhaha.yueyishou.common.result.Result;
import com.ilhaha.yueyishou.model.vo.tencentcloud.CosUploadVo;
import com.ilhaha.yueyishou.recycler.service.CosService;
import com.ilhaha.yueyishou.tencentcloud.client.CosFeignClient;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

/**
 * @Author ilhaha
 * @Create 2024/10/10 17:14
 * @Version 1.0
 */
@Service
public class CosServiceImpl implements CosService {

    @Resource
    private CosFeignClient cosFeignClient;

    /**
     * 上传文件
     * @param file
     * @param path
     * @return
     */
    @Override
    public Result<CosUploadVo> upload(MultipartFile file, String path) {
        // 获取当前时间
        String date = DateUtils.format(new Date(), "yyyy-MM-dd");
        return cosFeignClient.upload(file,path + "/" + date);
    }
}
