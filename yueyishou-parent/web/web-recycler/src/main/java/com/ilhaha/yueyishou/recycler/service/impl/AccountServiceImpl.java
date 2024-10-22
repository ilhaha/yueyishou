package com.ilhaha.yueyishou.recycler.service.impl;

import com.ilhaha.yueyishou.common.result.Result;
import com.ilhaha.yueyishou.common.util.AuthContextHolder;
import com.ilhaha.yueyishou.model.form.recycler.RecyclerAccountForm;
import com.ilhaha.yueyishou.model.form.recycler.RecyclerWithdrawForm;
import com.ilhaha.yueyishou.model.vo.recycler.RecyclerAccountVo;
import com.ilhaha.yueyishou.recycler.client.RecyclerAccountFeignClient;
import com.ilhaha.yueyishou.recycler.service.AccountService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * @Author ilhaha
 * @Create 2024/10/22 14:34
 * @Version 1.0
 */
@Service
public class AccountServiceImpl implements AccountService {

    @Resource
    private RecyclerAccountFeignClient recyclerAccountFeignClient;

    /**
     * 获取回收员账户信息
     * @param recyclerAccountForm
     * @return
     */
    @Override
    public Result<RecyclerAccountVo> getRecyclerAccountInfo(RecyclerAccountForm recyclerAccountForm) {
        recyclerAccountForm.setRecyclerId(AuthContextHolder.getRecyclerId());
        return recyclerAccountFeignClient.getRecyclerAccountInfo(recyclerAccountForm);
    }

    /**
     * 回收员给账号充值
     * @param recyclerWithdrawForm
     * @return
     */
    @Override
    public Result<Boolean> onRecharge(RecyclerWithdrawForm recyclerWithdrawForm) {
        recyclerWithdrawForm.setRecyclerId(AuthContextHolder.getRecyclerId());
        return recyclerAccountFeignClient.onRecharge(recyclerWithdrawForm);
    }


    /**
     * 回收员提现到微信零钱
     * @param recyclerWithdrawForm
     * @return
     */
    @Override
    public Result<Boolean> onWithdraw(RecyclerWithdrawForm recyclerWithdrawForm) {
        recyclerWithdrawForm.setRecyclerId(AuthContextHolder.getRecyclerId());
        return recyclerAccountFeignClient.onWithdraw(recyclerWithdrawForm);
    }
}
