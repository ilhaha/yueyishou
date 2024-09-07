package com.ilhaha.yueyishou.customer;

import com.ilhaha.yueyishou.result.Result;
import com.ilhaha.yueyishou.service.CustomerService;
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
     * 小程序登录
     * @param code
     * @return
     */
    @GetMapping("/login/{code}")
    public Result<String> login(@PathVariable("code") String code){
        return customerService.login(code);
    }

}
