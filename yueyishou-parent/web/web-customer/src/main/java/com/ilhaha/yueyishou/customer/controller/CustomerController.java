package com.ilhaha.yueyishou.customer.controller;

import com.ilhaha.yueyishou.common.anno.LoginVerification;
import com.ilhaha.yueyishou.common.result.Result;
import com.ilhaha.yueyishou.customer.service.CustomerService;
import com.ilhaha.yueyishou.model.vo.customer.CustomerLoginInfoVo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author ilhaha
 * @Create 2024/9/7 20:29
 * @Version 1.0
 */
@RestController
@RequestMapping("/customer")
public class CustomerController {

    @Resource
    private CustomerService customerService;

    /**
     * 获取顾客登录之后的顾客信息
     * @return
     */
    @LoginVerification
    @GetMapping("/login/info")
    public Result<CustomerLoginInfoVo> getLoginInfo(){
        return customerService.getLoginInfo();
    }


    /**
     * 小程序登录
     * @param code
     * @return
     */
    @GetMapping("/login/{code}")
    public Result<String> login(@PathVariable("code") String code){
        return customerService.login(code);
    }

}
