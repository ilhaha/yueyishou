package com.ilhaha.yueyishou.customer.service;

import com.ilhaha.yueyishou.common.result.Result;
import com.ilhaha.yueyishou.model.vo.cos.CosUploadVo;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author ilhaha
 * @Create 2024/9/24 10:38
 * @Version 1.0
 */
public interface CosService {

    /**
     * 上传文件
     * @param file
     * @param path
     * @return
     */
    Result<CosUploadVo> upload(MultipartFile file, String path);
}
