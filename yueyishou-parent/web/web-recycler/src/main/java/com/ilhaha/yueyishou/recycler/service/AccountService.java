package com.ilhaha.yueyishou.recycler.service;

import com.ilhaha.yueyishou.common.result.Result;
import com.ilhaha.yueyishou.model.form.recycler.RecyclerAccountForm;
import com.ilhaha.yueyishou.model.form.recycler.RecyclerWithdrawForm;
import com.ilhaha.yueyishou.model.vo.recycler.RecyclerAccountVo;

/**
 * @Author ilhaha
 * @Create 2024/10/22 14:34
 * @Version 1.0
 */
public interface AccountService {
    /**
     * 获取回收员账户信息
     * @param recyclerAccountForm
     * @return
     */
    Result<RecyclerAccountVo> getRecyclerAccountInfo(RecyclerAccountForm recyclerAccountForm);

    /**
     * 回收员给账号充值
     * @param recyclerWithdrawForm
     * @return
     */
    Result<Boolean> onRecharge(RecyclerWithdrawForm recyclerWithdrawForm);


    /**
     * 回收员提现到微信零钱
     * @param recyclerWithdrawForm
     * @return
     */
    Result<Boolean> onWithdraw(RecyclerWithdrawForm recyclerWithdrawForm);
}
