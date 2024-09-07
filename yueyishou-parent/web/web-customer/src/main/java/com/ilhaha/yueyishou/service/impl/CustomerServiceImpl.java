package com.ilhaha.yueyishou.service.impl;

import com.ilhaha.yueyishou.constant.RedisConstant;
import com.ilhaha.yueyishou.customer.client.CustomerInfoFeignClient;
import com.ilhaha.yueyishou.execption.YueYiShouException;
import com.ilhaha.yueyishou.result.Result;
import com.ilhaha.yueyishou.result.ResultCodeEnum;
import com.ilhaha.yueyishou.service.CustomerService;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @Author ilhaha
 * @Create 2024/9/7 20:33
 * @Version 1.0
 */
@Service
public class CustomerServiceImpl implements CustomerService {

    @Resource
    private CustomerInfoFeignClient customerInfoFeignClient;



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
}
