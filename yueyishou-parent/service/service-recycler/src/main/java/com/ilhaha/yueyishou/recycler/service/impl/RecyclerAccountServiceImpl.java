package com.ilhaha.yueyishou.recycler.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ilhaha.yueyishou.model.constant.AccountDetailsConstant;
import com.ilhaha.yueyishou.model.entity.recycler.RecyclerAccount;
import com.ilhaha.yueyishou.model.form.recycler.AddDetailsForm;
import com.ilhaha.yueyishou.model.form.recycler.RecyclerAccountForm;
import com.ilhaha.yueyishou.model.form.recycler.RecyclerWithdrawForm;
import com.ilhaha.yueyishou.model.vo.recycler.RecyclerAccountVo;
import com.ilhaha.yueyishou.recycler.mapper.RecyclerAccountMapper;
import com.ilhaha.yueyishou.recycler.service.IRecyclerAccountDetailService;
import com.ilhaha.yueyishou.recycler.service.IRecyclerAccountService;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecyclerAccountServiceImpl extends ServiceImpl<RecyclerAccountMapper, RecyclerAccount> implements IRecyclerAccountService {


    @Resource
    private IRecyclerAccountDetailService recyclerAccountDetailService;

    /**
     * 批量给回收员创建账户
     * @param recyclerIds
     */
    @Override
    public void createAccount(List<Long> recyclerIds) {
       this.saveBatch( recyclerIds.stream().map(item -> {
           RecyclerAccount recyclerAccount = new RecyclerAccount();
           recyclerAccount.setRecyclerId(item);
           return recyclerAccount;
       }).collect(Collectors.toList()));
    }

    /**
     * 获取回收员账户信息
     * @param recyclerAccountForm
     * @return
     */
    @Override
    public RecyclerAccountVo    getRecyclerAccountInfo(RecyclerAccountForm recyclerAccountForm) {
        RecyclerAccountVo recyclerAccountVo = new RecyclerAccountVo();
        RecyclerAccount recyclerAccountDB = this.getOne(new LambdaQueryWrapper<RecyclerAccount>().eq(RecyclerAccount::getRecyclerId, recyclerAccountForm.getRecyclerId()));
        BeanUtils.copyProperties(recyclerAccountDB,recyclerAccountVo);
        // 获取明细信息
        recyclerAccountVo.setRecyclerAccountDetailVoList(recyclerAccountDetailService.getRecyclerAccountDetail(recyclerAccountDB.getId(),recyclerAccountForm.getDetailsTime()));
        return recyclerAccountVo;
    }

    /**
     * 根据回收员id查询回收员账户
     * @param recyclerId
     * @return
     */
    @Override
    public RecyclerAccount getOneByRecyclerId(Long recyclerId) {
        return this.getOne(new LambdaQueryWrapper<RecyclerAccount>().eq(RecyclerAccount::getRecyclerId,recyclerId));
    }

    /**
     * 回收员给账号充值
     * @param recyclerWithdrawForm
     * @return
     */
    @Transactional
    @Override
    public Boolean onRecharge(RecyclerWithdrawForm recyclerWithdrawForm) {
        // 1. 查询当前账户余额
        RecyclerAccount account = this.getOneByRecyclerId(recyclerWithdrawForm.getRecyclerId());

        // 2. 计算新的余额
        BigDecimal newTotalAmount = account.getTotalAmount().add(recyclerWithdrawForm.getAmount());

        // 3. 更新账户余额
        LambdaUpdateWrapper<RecyclerAccount> recyclerAccountLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        recyclerAccountLambdaUpdateWrapper.eq(RecyclerAccount::getRecyclerId, recyclerWithdrawForm.getRecyclerId())
                .set(RecyclerAccount::getTotalAmount, newTotalAmount)
                .set(RecyclerAccount::getUpdateTime, new Date());

        // 4.添加明细数据
        AddDetailsForm addDetailsForm = new AddDetailsForm();
        addDetailsForm.setAccountId(account.getId());
        addDetailsForm.setAmount(recyclerWithdrawForm.getAmount());
        addDetailsForm.setTradeType(AccountDetailsConstant.ON_RECHARGE);
        addDetailsForm.setContent(AccountDetailsConstant.ON_RECHARGE_CONTENT);
        recyclerAccountDetailService.addDetails(addDetailsForm);

        return this.update(recyclerAccountLambdaUpdateWrapper);
    }

    /**
     * 回收员提现到微信零钱
     * @param recyclerWithdrawForm
     * @return
     */
    @Override
    public Boolean onWithdraw(RecyclerWithdrawForm recyclerWithdrawForm) {
        // 1. 查询当前账户余额
        RecyclerAccount account = this.getOneByRecyclerId(recyclerWithdrawForm.getRecyclerId());

        // 2. 计算新的余额
        BigDecimal newTotalAmount = account.getTotalAmount().subtract(recyclerWithdrawForm.getAmount());

        // 3. 更新账户余额
        LambdaUpdateWrapper<RecyclerAccount> recyclerAccountLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        recyclerAccountLambdaUpdateWrapper.eq(RecyclerAccount::getRecyclerId, recyclerWithdrawForm.getRecyclerId())
                .set(RecyclerAccount::getTotalAmount, newTotalAmount)
                .set(RecyclerAccount::getUpdateTime, new Date());

        // 4.添加明细数据
        AddDetailsForm addDetailsForm = new AddDetailsForm();
        addDetailsForm.setAccountId(account.getId());
        addDetailsForm.setAmount(recyclerWithdrawForm.getAmount());
        addDetailsForm.setTradeType(AccountDetailsConstant.ON_WITHDRAW);
        addDetailsForm.setContent(AccountDetailsConstant.ON_WITHDRAW_CONTENT);
        recyclerAccountDetailService.addDetails(addDetailsForm);
        return this.update(recyclerAccountLambdaUpdateWrapper);
    }

    /**
     * 结算订单
     * @param recyclerWithdrawForm
     * @return
     */
    @Override
    public Boolean settlement(RecyclerWithdrawForm recyclerWithdrawForm) {
        // 1. 查询当前账户余额
        RecyclerAccount account = this.getOneByRecyclerId(recyclerWithdrawForm.getRecyclerId());

        // 2. 计算新的余额
        BigDecimal newTotalAmount = account.getTotalAmount().subtract(recyclerWithdrawForm.getAmount());

        // 3. 更新账户余额
        LambdaUpdateWrapper<RecyclerAccount> recyclerAccountLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        recyclerAccountLambdaUpdateWrapper.eq(RecyclerAccount::getRecyclerId, recyclerWithdrawForm.getRecyclerId())
                .set(RecyclerAccount::getTotalAmount, newTotalAmount)
                .set(RecyclerAccount::getUpdateTime, new Date());

        // 4.添加明细数据
        AddDetailsForm addDetailsForm = new AddDetailsForm();
        addDetailsForm.setAccountId(account.getId());
        addDetailsForm.setAmount(recyclerWithdrawForm.getAmount());
        addDetailsForm.setTradeType(AccountDetailsConstant.RECYCLE_PAY_FEE);
        addDetailsForm.setContent(AccountDetailsConstant.RECYCLE_PAY_FEE_CONTENT);
        recyclerAccountDetailService.addDetails(addDetailsForm);
        return this.update(recyclerAccountLambdaUpdateWrapper);
    }
}
