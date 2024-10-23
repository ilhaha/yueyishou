package com.ilhaha.yueyishou.customer.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ilhaha.yueyishou.model.entity.customer.CustomerAccount;
import com.ilhaha.yueyishou.model.form.customer.CustomerAccountForm;
import com.ilhaha.yueyishou.model.form.customer.CustomerWithdrawForm;
import com.ilhaha.yueyishou.model.vo.customer.CustomerAccountVo;

public interface ICustomerAccountService extends IService<CustomerAccount> {

    /**
     * 结算顾客订单
     * @param customerWithdrawForm
     * @return
     */
    Boolean settlement(CustomerWithdrawForm customerWithdrawForm);

    /**
     * 获取顾客账户信息
     * @param customerAccountForm
     * @return
     */
    CustomerAccountVo getCustomerAccountInfo(CustomerAccountForm customerAccountForm);

    /**
     * 顾客提现到微信零钱
     * @param customerWithdrawForm
     * @return
     */
    Boolean onWithdraw(CustomerWithdrawForm customerWithdrawForm);
}
