package com.ilhaha.yueyishou.customer.service;

import com.ilhaha.yueyishou.common.result.Result;
import com.ilhaha.yueyishou.model.form.customer.UpdateCustomerBaseInfoForm;
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

    /**
     * 更新顾客基本信息
     * @param updateCustomerBaseInfoForm
     * @return
     */
    Result<Boolean> updateBaseInfo(UpdateCustomerBaseInfoForm updateCustomerBaseInfoForm);

    /**
     * 以防如果用户还未退出登录就已经认证成功成为回收员出现信息不全问题
     * 也就是redis中无该回收员Id
     * @param token
     * @return
     */
    Result<Boolean> replenishInfo(String token);
}
