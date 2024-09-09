package org.jeecg.modules.mgr.service.impl;

import com.ilhaha.yueyishou.tencentcloud.client.CosFeignClient;
import com.ilhaha.yueyishou.vo.recycler.CosUploadVo;
import org.jeecg.modules.mgr.service.CosService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * @Author ilhaha
 * @Create 2024/9/8 20:06
 * @Version 1.0
 */
@Service
public class CosServiceImpl implements CosService {

    @Resource
    private CosFeignClient cosFeignClient;

    /**
     * 文件上传
     * @param file
     * @param path
     * @return
     */
    @Override
    public CosUploadVo upload(MultipartFile file, String path) {
        return cosFeignClient.upload(file,path).getData();
    }

    /**
     * 获取腾讯云图片临时访问路径
     * @return
     */
    @Override
    public String getImageUrl(String path) {
        return cosFeignClient.getImageUrl(path).getData();
    }
}
