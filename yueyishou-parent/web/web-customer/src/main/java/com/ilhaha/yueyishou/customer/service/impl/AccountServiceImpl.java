package com.ilhaha.yueyishou.customer.service.impl;

import com.ilhaha.yueyishou.common.result.Result;
import com.ilhaha.yueyishou.common.util.AuthContextHolder;
import com.ilhaha.yueyishou.customer.client.CustomerAccountFeignClient;
import com.ilhaha.yueyishou.customer.service.AccountService;
import com.ilhaha.yueyishou.model.form.customer.CustomerAccountForm;
import com.ilhaha.yueyishou.model.form.customer.CustomerWithdrawForm;
import com.ilhaha.yueyishou.model.vo.customer.CustomerAccountVo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * @Author ilhaha
 * @Create 2024/10/23 23:19
 * @Version 1.0
 */
@Service
public class AccountServiceImpl implements AccountService {

    @Resource
    private CustomerAccountFeignClient customerAccountFeignClient;

    /**
     * 获取顾客账户信息
     * @param customerAccountForm
     * @return
     */
    @Override
    public Result<CustomerAccountVo> getCustomerAccountInfo(CustomerAccountForm customerAccountForm) {
        customerAccountForm.setCustomerId(AuthContextHolder.getCustomerId());
        return customerAccountFeignClient.getCustomerAccountInfo(customerAccountForm);
    }

    /**
     * 顾客提现到微信零钱
     * @param customerWithdrawForm
     * @return
     */
    @Override
    public Result<Boolean> onWithdraw(CustomerWithdrawForm customerWithdrawForm) {
        customerWithdrawForm.setCustomerId(AuthContextHolder.getCustomerId());
        return customerAccountFeignClient.onWithdraw(customerWithdrawForm);
    }
}
