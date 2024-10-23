package com.ilhaha.yueyishou.customer.controller;

import com.ilhaha.yueyishou.common.anno.LoginVerification;
import com.ilhaha.yueyishou.common.result.Result;
import com.ilhaha.yueyishou.customer.service.AccountService;
import com.ilhaha.yueyishou.model.form.customer.CustomerAccountForm;
import com.ilhaha.yueyishou.model.form.customer.CustomerWithdrawForm;
import com.ilhaha.yueyishou.model.vo.customer.CustomerAccountVo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author ilhaha
 * @Create 2024/10/23 23:18
 * @Version 1.0
 */
@RestController
@RequestMapping("/account")
public class AccountController {

    @Resource
    private AccountService accountService;

    /**
     * 顾客提现到微信零钱
     * @param customerWithdrawForm
     * @return
     */
    @LoginVerification
    @PostMapping("/onWithdraw")
    public Result<Boolean> onWithdraw(@RequestBody CustomerWithdrawForm customerWithdrawForm){
        return accountService.onWithdraw(customerWithdrawForm);
    }

    /**
     * 获取顾客账户信息
     * @param customerAccountForm
     * @return
     */
    @LoginVerification
    @PostMapping("/info")
    public Result<CustomerAccountVo> getCustomerAccountInfo(@RequestBody CustomerAccountForm customerAccountForm){
        return accountService.getCustomerAccountInfo(customerAccountForm);
    }
}
