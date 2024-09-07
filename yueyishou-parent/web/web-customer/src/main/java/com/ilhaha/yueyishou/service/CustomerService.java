package com.ilhaha.yueyishou.service;

import com.ilhaha.yueyishou.result.Result;

/**
 * @Author ilhaha
 * @Create 2024/9/7 20:32
 * @Version 1.0
 */
public interface CustomerService {

    /**
     * 小程序登录
     * @param code
     * @return
     */
    Result<String> login(String code);
}
