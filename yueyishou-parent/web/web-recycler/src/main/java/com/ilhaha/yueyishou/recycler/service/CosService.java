package com.ilhaha.yueyishou.recycler.service;

import com.ilhaha.yueyishou.common.result.Result;
import com.ilhaha.yueyishou.model.vo.tencentcloud.CosUploadVo;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author ilhaha
 * @Create 2024/10/10 17:14
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
