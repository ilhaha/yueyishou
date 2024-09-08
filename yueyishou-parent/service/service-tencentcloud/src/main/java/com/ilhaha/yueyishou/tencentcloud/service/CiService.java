package com.ilhaha.yueyishou.tencentcloud.service;

/**
 * @Author ilhaha
 * @Create 2024/9/8 15:36
 * @Version 1.0
 */
public interface CiService {
    /**
     * 图片审核
     * @param path
     * @return
     */
    Boolean imageAuditing(String path);
}
