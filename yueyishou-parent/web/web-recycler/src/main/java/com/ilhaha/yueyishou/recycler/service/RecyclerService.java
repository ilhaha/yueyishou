package com.ilhaha.yueyishou.recycler.service;

import com.ilhaha.yueyishou.result.Result;
import com.ilhaha.yueyishou.vo.recycler.CosUploadVo;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author ilhaha
 * @Create 2024/9/8 17:20
 * @Version 1.0
 */
public interface RecyclerService {

    /**
     * 回收员上传图片
     * @param file
     * @return
     */
    Result<CosUploadVo> upload(MultipartFile file, String path);
}
