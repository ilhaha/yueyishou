package com.ilhaha.yueyishou.customer.service;

import com.ilhaha.yueyishou.common.result.Result;
import com.ilhaha.yueyishou.model.form.customer.CustomerAccountForm;
import com.ilhaha.yueyishou.model.form.customer.CustomerWithdrawForm;
import com.ilhaha.yueyishou.model.vo.customer.CustomerAccountVo;

/**
 * @Author ilhaha
 * @Create 2024/10/23 23:18
 * @Version 1.0
 */
public interface AccountService {

    /**
     * 获取顾客账户信息
     * @param customerAccountForm
     * @return
     */
    Result<CustomerAccountVo> getCustomerAccountInfo(CustomerAccountForm customerAccountForm);

    /**
     * 顾客提现到微信零钱
     * @param customerWithdrawForm
     * @return
     */
    Result<Boolean> onWithdraw(CustomerWithdrawForm customerWithdrawForm);
}
