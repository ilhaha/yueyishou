package com.ilhaha.yueyishou.recycler.controller;

import com.ilhaha.yueyishou.common.anno.LoginVerification;
import com.ilhaha.yueyishou.common.result.Result;
import com.ilhaha.yueyishou.model.form.recycler.RecyclerAccountForm;
import com.ilhaha.yueyishou.model.form.recycler.RecyclerWithdrawForm;
import com.ilhaha.yueyishou.model.vo.recycler.RecyclerAccountVo;
import com.ilhaha.yueyishou.recycler.service.AccountService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

/**
 * @Author ilhaha
 * @Create 2024/10/22 14:33
 * @Version 1.0
 */
@RestController
@RequestMapping("/account")
public class AccountController {

    @Resource
    private AccountService accountService;

    /**
     * 回收员提现到微信零钱
     * @param recyclerWithdrawForm
     * @return
     */
    @LoginVerification
    @PostMapping("/onWithdraw")
    public Result<Boolean> onWithdraw(@RequestBody RecyclerWithdrawForm recyclerWithdrawForm){
        return accountService.onWithdraw(recyclerWithdrawForm);
    }


    /**
     * 回收员给账号充值
     * @param recyclerWithdrawForm
     * @return
     */
    @LoginVerification
    @PostMapping("/onRecharge")
    public Result<Boolean> onRecharge(@RequestBody RecyclerWithdrawForm recyclerWithdrawForm){
        return accountService.onRecharge(recyclerWithdrawForm);
    }

    /**
     * 获取回收员账户信息
     * @param recyclerAccountForm
     * @return
     */
    @LoginVerification
    @PostMapping("/info")
    public Result<RecyclerAccountVo> getRecyclerAccountInfo(@RequestBody RecyclerAccountForm recyclerAccountForm){
        return accountService.getRecyclerAccountInfo(recyclerAccountForm);
    }
}
