package com.ilhaha.yueyishou.customer.service;

import com.ilhaha.yueyishou.common.result.Result;
import com.ilhaha.yueyishou.model.vo.customer.CustomerLoginInfoVo;

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

    /**
     * 获取顾客登录之后的顾客信息
     * @return
     */
    Result<CustomerLoginInfoVo> getLoginInfo();

}
