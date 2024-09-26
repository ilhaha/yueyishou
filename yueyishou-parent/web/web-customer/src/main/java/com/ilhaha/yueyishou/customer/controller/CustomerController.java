package com.ilhaha.yueyishou.customer.controller;

import com.ilhaha.yueyishou.common.anno.LoginVerification;
import com.ilhaha.yueyishou.common.result.Result;
import com.ilhaha.yueyishou.customer.service.CustomerService;
import com.ilhaha.yueyishou.model.form.recycler.RecyclerApplyForm;
import com.ilhaha.yueyishou.model.vo.customer.CustomerLoginInfoVo;
import com.ilhaha.yueyishou.model.vo.recycler.RecyclerAuthImagesVo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

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
     * 根据顾客Id获取回收员认证图片信息
     * @return
     */
    @LoginVerification
    @GetMapping("/get/authImages")
    public Result<RecyclerAuthImagesVo> getAuthImages(){
        return customerService.getAuthImages();
    }

    /**
     * 认证成为回收员
     * @param recyclerApplyForm
     * @return
     */
    @LoginVerification
    @PostMapping("/auth/recycler")
    public Result<Boolean> authRecycler(@RequestBody RecyclerApplyForm recyclerApplyForm){
        System.out.println(recyclerApplyForm);
        return customerService.authRecycler(recyclerApplyForm);
    }


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
