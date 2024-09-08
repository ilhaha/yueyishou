package com.ilhaha.yueyishou.recycler.service.impl;

import com.ilhaha.yueyishou.recycler.client.RecyclerInfoFeignClient;
import com.ilhaha.yueyishou.recycler.service.RecyclerService;
import com.ilhaha.yueyishou.result.Result;
import com.ilhaha.yueyishou.tencentcloud.client.CosFeignClient;
import com.ilhaha.yueyishou.vo.recycler.CosUploadVo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author ilhaha
 * @Create 2024/9/8 17:20
 * @Version 1.0
 */
@Service
public class RecyclerServiceImpl implements RecyclerService {

    @Resource
    private CosFeignClient cosFeignClient;

    @Override
    public Result<CosUploadVo> upload(MultipartFile file, String path) {
        return cosFeignClient.upload(file,path);
    }
}
