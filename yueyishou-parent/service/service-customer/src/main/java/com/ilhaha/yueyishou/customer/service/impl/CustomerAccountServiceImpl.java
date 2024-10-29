package com.ilhaha.yueyishou.customer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ilhaha.yueyishou.customer.service.ICustomerAccountDetailService;
import com.ilhaha.yueyishou.model.constant.AccountDetailsConstant;
import com.ilhaha.yueyishou.model.entity.customer.CustomerAccount;
import com.ilhaha.yueyishou.customer.mapper.CustomerAccountMapper;
import com.ilhaha.yueyishou.customer.service.ICustomerAccountService;
import com.ilhaha.yueyishou.model.entity.recycler.RecyclerAccount;
import com.ilhaha.yueyishou.model.form.customer.CustomerAccountForm;
import com.ilhaha.yueyishou.model.form.customer.CustomerWithdrawForm;
import com.ilhaha.yueyishou.model.form.recycler.AddDetailsForm;
import com.ilhaha.yueyishou.model.vo.customer.CustomerAccountVo;
import jakarta.annotation.Resource;
import org.checkerframework.checker.units.qual.C;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;

@Service
public class CustomerAccountServiceImpl extends ServiceImpl<CustomerAccountMapper, CustomerAccount> implements ICustomerAccountService {


    @Resource
    private ICustomerAccountDetailService customerAccountDetailService;

    /**
     * 结算顾客订单
     * @param customerWithdrawForm
     * @return
     */
    @Transactional
    @Override
    public Boolean settlement(CustomerWithdrawForm customerWithdrawForm) {
        // 1. 查询顾客账户
        CustomerAccount customerAccountDB = this.getOne(new LambdaQueryWrapper<CustomerAccount>().eq(CustomerAccount::getCustomerId, customerWithdrawForm.getCustomerId()));

        // 2. 计算新的余额
        BigDecimal newTotalAmount = customerAccountDB.getTotalAmount().add(customerWithdrawForm.getAmount());
        BigDecimal newTotalRecycleIncome = customerAccountDB.getTotalRecycleIncome().add(customerWithdrawForm.getAmount());

        // 3. 更新账户余额
        LambdaUpdateWrapper<CustomerAccount> customerAccountLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        customerAccountLambdaUpdateWrapper.eq(CustomerAccount::getCustomerId, customerWithdrawForm.getCustomerId())
                .set(CustomerAccount::getTotalAmount, newTotalAmount)
                .set(CustomerAccount::getTotalRecycleIncome,newTotalRecycleIncome)
                .set(CustomerAccount::getUpdateTime, new Date());

        // 4.增加明细
        AddDetailsForm addDetailsForm = new AddDetailsForm();
        addDetailsForm.setAccountId(customerAccountDB.getId());
        addDetailsForm.setAmount(customerWithdrawForm.getAmount());
        addDetailsForm.setTradeType(AccountDetailsConstant.INCOME);
        addDetailsForm.setContent(AccountDetailsConstant.RECYCLE_PAY_FEE_CONTENT);
        customerAccountDetailService.addDetails(addDetailsForm);

        return this.update(customerAccountLambdaUpdateWrapper);
    }

    /**
     * 获取顾客账户信息
     * @param customerAccountForm
     * @return
     */
    @Override
    public CustomerAccountVo getCustomerAccountInfo(CustomerAccountForm customerAccountForm) {
        CustomerAccountVo customerAccountVo = new CustomerAccountVo();
        CustomerAccount customerAccountDB = this.getOne(new LambdaQueryWrapper<CustomerAccount>().eq(CustomerAccount::getCustomerId, customerAccountForm.getCustomerId()));
        BeanUtils.copyProperties(customerAccountDB,customerAccountVo);
        // 获取账户明细信息
        customerAccountVo.setCustomerAccountDetailVoList(customerAccountDetailService.getCustomerAccountDetail(customerAccountDB.getId(),customerAccountForm.getDetailsTime()));
        return customerAccountVo;
    }

    /**
     * 顾客提现到微信零钱
     * @param customerWithdrawForm
     * @return
     */
    @Override
    public Boolean onWithdraw(CustomerWithdrawForm customerWithdrawForm) {
        // 1. 查询顾客账户
        CustomerAccount customerAccountDB = this.getOne(new LambdaQueryWrapper<CustomerAccount>().eq(CustomerAccount::getCustomerId, customerWithdrawForm.getCustomerId()));

        // 2. 计算新的余额
        BigDecimal newTotalAmount = customerAccountDB.getTotalAmount().subtract(customerWithdrawForm.getAmount());

        // 3. 更新账户余额
        LambdaUpdateWrapper<CustomerAccount> customerAccountLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        customerAccountLambdaUpdateWrapper.eq(CustomerAccount::getCustomerId, customerWithdrawForm.getCustomerId())
                .set(CustomerAccount::getTotalAmount, newTotalAmount)
                .set(CustomerAccount::getUpdateTime, new Date());

        // 4.添加明细数据
        AddDetailsForm addDetailsForm = new AddDetailsForm();
        addDetailsForm.setAccountId(customerAccountDB.getId());
        addDetailsForm.setAmount(customerWithdrawForm.getAmount());
        addDetailsForm.setTradeType(AccountDetailsConstant.EXPENDITURE);
        addDetailsForm.setContent(AccountDetailsConstant.ON_WITHDRAW_CONTENT);
        customerAccountDetailService.addDetails(addDetailsForm);

        return this.update(customerAccountLambdaUpdateWrapper);
    }

    /**
     * 超过预约时间未到达，需回收员赔偿取消
     * @param customerWithdrawForm
     * @return
     */
    @Override
    public Boolean cancelOrderIfOverdue(CustomerWithdrawForm customerWithdrawForm) {
        // 1. 查询顾客账户
        CustomerAccount customerAccountDB = this.getOne(new LambdaQueryWrapper<CustomerAccount>().eq(CustomerAccount::getCustomerId, customerWithdrawForm.getCustomerId()));

        // 2. 计算新的余额
        BigDecimal newTotalAmount = customerAccountDB.getTotalAmount().add(customerWithdrawForm.getAmount());

        // 3. 更新账户余额
        LambdaUpdateWrapper<CustomerAccount> customerAccountLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        customerAccountLambdaUpdateWrapper.eq(CustomerAccount::getCustomerId, customerWithdrawForm.getCustomerId())
                .set(CustomerAccount::getTotalAmount, newTotalAmount)
                .set(CustomerAccount::getUpdateTime, new Date());

        // 4.增加明细
        AddDetailsForm addDetailsForm = new AddDetailsForm();
        addDetailsForm.setAccountId(customerAccountDB.getId());
        addDetailsForm.setAmount(customerWithdrawForm.getAmount());
        addDetailsForm.setTradeType(AccountDetailsConstant.INCOME);
        addDetailsForm.setContent(AccountDetailsConstant.TIMEOUT_SERVICE_COMPENSATION);
        customerAccountDetailService.addDetails(addDetailsForm);

        return this.update(customerAccountLambdaUpdateWrapper);
    }

    /**
     * 顾客取消已超过免费时限，需支付相关费用取消订单
     * @param customerWithdrawForm
     * @return
     */
    @Transactional
    @Override
    public Boolean processPaidCancellation(CustomerWithdrawForm customerWithdrawForm) {
        // 1. 查询顾客账户
        CustomerAccount customerAccountDB = this.getOne(new LambdaQueryWrapper<CustomerAccount>().eq(CustomerAccount::getCustomerId, customerWithdrawForm.getCustomerId()));

        // 2. 计算新的余额
        BigDecimal newTotalAmount = customerAccountDB.getTotalAmount().subtract(customerWithdrawForm.getAmount());

        // 3. 更新账户余额
        LambdaUpdateWrapper<CustomerAccount> customerAccountLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        customerAccountLambdaUpdateWrapper.eq(CustomerAccount::getCustomerId, customerWithdrawForm.getCustomerId())
                .set(CustomerAccount::getTotalAmount, newTotalAmount)
                .set(CustomerAccount::getUpdateTime, new Date());

        // 4.添加明细数据
        AddDetailsForm addDetailsForm = new AddDetailsForm();
        addDetailsForm.setAccountId(customerAccountDB.getId());
        addDetailsForm.setAmount(customerWithdrawForm.getAmount());
        addDetailsForm.setTradeType(AccountDetailsConstant.EXPENDITURE);
        addDetailsForm.setContent(AccountDetailsConstant.OVERDUE_CANCELLATION_COMPENSATION);
        customerAccountDetailService.addDetails(addDetailsForm);

        return this.update(customerAccountLambdaUpdateWrapper);
    }

    /**
     * 回收员距离预约时间不足60分钟付费取消
     * @param customerWithdrawForm
     * @return
     */
    @Transactional
    @Override
    public Boolean chargeCancellationIfWithinOneHour(CustomerWithdrawForm customerWithdrawForm) {
        // 1. 查询顾客账户
        CustomerAccount customerAccountDB = this.getOne(new LambdaQueryWrapper<CustomerAccount>().eq(CustomerAccount::getCustomerId, customerWithdrawForm.getCustomerId()));

        // 2. 计算新的余额
        BigDecimal newTotalAmount = customerAccountDB.getTotalAmount().add(customerWithdrawForm.getAmount());

        // 3. 更新账户余额
        LambdaUpdateWrapper<CustomerAccount> customerAccountLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        customerAccountLambdaUpdateWrapper.eq(CustomerAccount::getCustomerId, customerWithdrawForm.getCustomerId())
                .set(CustomerAccount::getTotalAmount, newTotalAmount)
                .set(CustomerAccount::getUpdateTime, new Date());

        // 4.增加明细
        AddDetailsForm addDetailsForm = new AddDetailsForm();
        addDetailsForm.setAccountId(customerAccountDB.getId());
        addDetailsForm.setAmount(customerWithdrawForm.getAmount());
        addDetailsForm.setTradeType(AccountDetailsConstant.INCOME);
        addDetailsForm.setContent(AccountDetailsConstant.CUSTOMER_SHORT_NOTICE_CANCELLATION);
        customerAccountDetailService.addDetails(addDetailsForm);

        return this.update(customerAccountLambdaUpdateWrapper);
    }
}
