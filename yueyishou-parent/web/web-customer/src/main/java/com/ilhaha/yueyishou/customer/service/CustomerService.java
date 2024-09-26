package com.ilhaha.yueyishou.customer.service;

import com.ilhaha.yueyishou.common.result.Result;
import com.ilhaha.yueyishou.model.form.recycler.RecyclerApplyForm;
import com.ilhaha.yueyishou.model.vo.customer.CustomerLoginInfoVo;
import com.ilhaha.yueyishou.model.vo.recycler.RecyclerAuthImagesVo;

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


    /**
     * 认证成为回收员
     * @param recyclerApplyForm
     * @return
     */
    Result<Boolean> authRecycler(RecyclerApplyForm recyclerApplyForm);

    /**
     * 根据顾客Id获取回收员认证图片信息
     * @return
     */
    Result<RecyclerAuthImagesVo> getAuthImages();

}
