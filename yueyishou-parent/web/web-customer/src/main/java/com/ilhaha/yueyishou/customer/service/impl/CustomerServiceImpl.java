package com.ilhaha.yueyishou.customer.service.impl;

import com.ilhaha.yueyishou.customer.client.CustomerInfoFeignClient;
import com.ilhaha.yueyishou.common.result.Result;
import com.ilhaha.yueyishou.customer.service.CustomerService;
import com.ilhaha.yueyishou.common.util.AuthContextHolder;
import com.ilhaha.yueyishou.model.form.recycler.RecyclerApplyForm;
import com.ilhaha.yueyishou.model.vo.customer.CustomerLoginInfoVo;
import com.ilhaha.yueyishou.model.vo.recycler.RecyclerAuthImagesVo;
import com.ilhaha.yueyishou.recycler.client.RecyclerInfoFeignClient;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * @Author ilhaha
 * @Create 2024/9/7 20:33
 * @Version 1.0
 */
@Service
public class CustomerServiceImpl implements CustomerService {

    @Resource
    private CustomerInfoFeignClient customerInfoFeignClient;

    @Resource
    private RecyclerInfoFeignClient recyclerInfoFeignClient;



    /**
     * 小程序登录
     * @param code
     * @return
     */
    @Override
    public Result<String> login(String code) {
        // 1.远程调用,获取顾客登录token
        return customerInfoFeignClient.login(code);
    }

    /**
     * 获取顾客登录之后的顾客信息
     * @return
     */
    @Override
    public Result<CustomerLoginInfoVo> getLoginInfo() {
        // 获取当前登录的顾客ID
        Long customerId = AuthContextHolder.getCustomerId();
        // 远程调用获取顾客信息
        return customerInfoFeignClient.getLoginInfo(customerId);
    }

    /**
     * 认证成为回收员
     * @param recyclerApplyForm
     * @return
     */
    @Override
    public Result<Boolean> authRecycler(RecyclerApplyForm recyclerApplyForm) {
        recyclerApplyForm.setCustomerId(AuthContextHolder.getCustomerId());
        return customerInfoFeignClient.authRecycler(recyclerApplyForm);
    }

    /**
     * 根据顾客Id获取回收员认证图片信息
     * @return
     */
    @Override
    public Result<RecyclerAuthImagesVo> getAuthImages() {
        return recyclerInfoFeignClient.getAuthImages(AuthContextHolder.getCustomerId());
    }
}
