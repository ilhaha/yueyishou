package org.jeecg.modules.mgr.service;

import com.ilhaha.yueyishou.model.vo.cos.CosUploadVo;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author ilhaha
 * @Create 2024/9/8 20:06
 * @Version 1.0
 */
public interface CosService {

    /**
     * 文件上传
     * @param file
     * @param path
     * @return
     */
    CosUploadVo upload(MultipartFile file, String path);

    /**
     * 获取腾讯云图片临时访问路径
     * @return
     */
    String getImageUrl(String path);
}
