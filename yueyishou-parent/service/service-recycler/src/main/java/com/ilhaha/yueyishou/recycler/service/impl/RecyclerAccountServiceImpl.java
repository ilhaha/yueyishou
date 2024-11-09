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
import com.ilhaha.yueyishou.recycler.service.IRecyclerInfoService;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RecyclerAccountServiceImpl extends ServiceImpl<RecyclerAccountMapper, RecyclerAccount> implements IRecyclerAccountService {


    @Resource
    private IRecyclerAccountDetailService recyclerAccountDetailService;

    @Lazy
    @Resource
    private IRecyclerInfoService recyclerInfoService;

    /**
     * 批量给回收员创建账户
     * @param recyclerIds
     */
    @Override
    public void createAccount(List<Long> recyclerIds) {
        // 1. 查询数据库中已经存在的 recyclerId
        LambdaQueryWrapper<RecyclerAccount> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(RecyclerAccount::getRecyclerId, recyclerIds);
        List<RecyclerAccount> existingAccounts = this.list(queryWrapper);

        // 2. 提取已存在的 recyclerId
        Set<Long> existingRecyclerIds = existingAccounts.stream()
                .map(RecyclerAccount::getRecyclerId)
                .collect(Collectors.toSet());

        // 3. 过滤掉已存在的 recyclerId 并创建新账户
        List<RecyclerAccount> newAccounts = recyclerIds.stream()
                .filter(item -> !existingRecyclerIds.contains(item))
                .map(item -> {
                    RecyclerAccount recyclerAccount = new RecyclerAccount();
                    recyclerAccount.setRecyclerId(item);
                    return recyclerAccount;
                })
                .collect(Collectors.toList());

        // 4. 批量保存新的账户
        if (!newAccounts.isEmpty()) {
            this.saveBatch(newAccounts);
        }
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
        addDetailsForm.setTradeType(AccountDetailsConstant.INCOME);
        addDetailsForm.setContent(AccountDetailsConstant.ON_RECHARGE_CONTENT);
        recyclerAccountDetailService.addDetails(addDetailsForm);

        return this.update(recyclerAccountLambdaUpdateWrapper);
    }

    /**
     * 回收员提现到微信零钱
     * @param recyclerWithdrawForm
     * @return
     */
    @Transactional
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
        addDetailsForm.setTradeType(AccountDetailsConstant.EXPENDITURE);
        addDetailsForm.setContent(AccountDetailsConstant.ON_WITHDRAW_CONTENT);
        recyclerAccountDetailService.addDetails(addDetailsForm);
        return this.update(recyclerAccountLambdaUpdateWrapper);
    }

    /**
     * 结算订单
     * @param recyclerWithdrawForm
     * @return
     */
    @Transactional
    @Override
    public Boolean settlement(RecyclerWithdrawForm recyclerWithdrawForm) {
        // 1. 查询当前账户余额
        RecyclerAccount account = this.getOneByRecyclerId(recyclerWithdrawForm.getRecyclerId());

        // 2. 计算新的余额
        BigDecimal newTotalAmount = account.getTotalAmount().subtract(recyclerWithdrawForm.getAmount());
        BigDecimal newTotalRecyclePay = account.getTotalRecyclePay().add(recyclerWithdrawForm.getAmount());

        // 3. 更新账户余额
        LambdaUpdateWrapper<RecyclerAccount> recyclerAccountLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        recyclerAccountLambdaUpdateWrapper.eq(RecyclerAccount::getRecyclerId, recyclerWithdrawForm.getRecyclerId())
                .set(RecyclerAccount::getTotalAmount, newTotalAmount)
                .set(RecyclerAccount::getTotalRecyclePay,newTotalRecyclePay)
                .set(RecyclerAccount::getUpdateTime, new Date());

        // 4.添加明细数据
        AddDetailsForm addDetailsForm = new AddDetailsForm();
        addDetailsForm.setAccountId(account.getId());
        addDetailsForm.setAmount(recyclerWithdrawForm.getAmount());
        addDetailsForm.setTradeType(AccountDetailsConstant.EXPENDITURE);
        addDetailsForm.setContent(AccountDetailsConstant.RECYCLE_PAY_FEE_CONTENT);
        recyclerAccountDetailService.addDetails(addDetailsForm);

        // 5.增加回收员单量
        recyclerInfoService.updateOrderCount(recyclerWithdrawForm.getRecyclerId());

        return this.update(recyclerAccountLambdaUpdateWrapper);
    }

    /**
     * 超过预约时间未到达，需回收员赔偿取消
     * @param recyclerWithdrawForm
     * @return
     */
    @Transactional
    @Override
    public Boolean cancelOrderIfOverdue(RecyclerWithdrawForm recyclerWithdrawForm) {
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
        addDetailsForm.setTradeType(AccountDetailsConstant.EXPENDITURE);
        addDetailsForm.setContent(AccountDetailsConstant.UNDELIVERED_SERVICE_COMPENSATION);
        recyclerAccountDetailService.addDetails(addDetailsForm);
        return this.update(recyclerAccountLambdaUpdateWrapper);
    }

    /**
     * 顾客取消已超过免费时限，需支付相关费用取消订单
     * @param recyclerWithdrawForm
     * @return
     */
    @Transactional
    @Override
    public Boolean processPaidCancellation(RecyclerWithdrawForm recyclerWithdrawForm) {
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
        addDetailsForm.setTradeType(AccountDetailsConstant.INCOME);
        addDetailsForm.setContent(AccountDetailsConstant.TIMEOUT_CANCELLATION_COMPENSATION);
        recyclerAccountDetailService.addDetails(addDetailsForm);

        return this.update(recyclerAccountLambdaUpdateWrapper);
    }

    /**
     * 回收员距离预约时间不足60分钟付费取消
     * @param recyclerWithdrawForm
     * @return
     */
    @Transactional
    @Override
    public Boolean chargeCancellationIfWithinOneHour(RecyclerWithdrawForm recyclerWithdrawForm) {
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
        addDetailsForm.setTradeType(AccountDetailsConstant.EXPENDITURE);
        addDetailsForm.setContent(AccountDetailsConstant.RECYCLER_SHORT_NOTICE_CANCELLATION);
        recyclerAccountDetailService.addDetails(addDetailsForm);
        return this.update(recyclerAccountLambdaUpdateWrapper);
    }

    /**
     * 回收员拒收订单得到补偿
     * @param recyclerWithdrawForm
     * @return
     */
    @Transactional
    @Override
    public Boolean rejectCompensate(RecyclerWithdrawForm recyclerWithdrawForm) {
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
        addDetailsForm.setTradeType(AccountDetailsConstant.INCOME);
        addDetailsForm.setContent(AccountDetailsConstant.RECYCLER_COMPENSATION_REJECTION);
        recyclerAccountDetailService.addDetails(addDetailsForm);
        return this.update(recyclerAccountLambdaUpdateWrapper);
    }
}
