package com.ilhaha.yueyishou.recycler.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ilhaha.yueyishou.model.entity.recycler.RecyclerAccount;
import com.ilhaha.yueyishou.model.form.recycler.RecyclerAccountForm;
import com.ilhaha.yueyishou.model.form.recycler.RecyclerWithdrawForm;
import com.ilhaha.yueyishou.model.vo.recycler.RecyclerAccountVo;

import java.util.List;

public interface IRecyclerAccountService extends IService<RecyclerAccount> {

    /**
     * 批量给回收员创建账户
     * @param recyclerIds
     */
    void createAccount(List<Long> recyclerIds);

    /**
     * 获取回收员账户信息
     * @param recyclerAccountForm
     * @return
     */
    RecyclerAccountVo getRecyclerAccountInfo(RecyclerAccountForm recyclerAccountForm);

    /**
     * 根据回收员id查询回收员账户
     * @param recyclerId
     * @return
     */
    RecyclerAccount getOneByRecyclerId(Long recyclerId);

    /**
     * 回收员给账号充值
     * @param recyclerWithdrawForm
     * @return
     */
    Boolean onRecharge(RecyclerWithdrawForm recyclerWithdrawForm);

    /**
     * 回收员提现到微信零钱
     * @param recyclerWithdrawForm
     * @return
     */
    Boolean onWithdraw(RecyclerWithdrawForm recyclerWithdrawForm);

    /**
     * 结算订单
     * @param recyclerWithdrawForm
     * @return
     */
    Boolean settlement(RecyclerWithdrawForm recyclerWithdrawForm);

    /**
     * 超过预约时间未到达，需回收员赔偿取消
     * @param recyclerWithdrawForm
     * @return
     */
    Boolean cancelOrderIfOverdue(RecyclerWithdrawForm recyclerWithdrawForm);

    /**
     * 顾客取消已超过免费时限，需支付相关费用取消订单
     * @param recyclerWithdrawForm
     * @return
     */
    Boolean processPaidCancellation(RecyclerWithdrawForm recyclerWithdrawForm);

    /**
     * 回收员距离预约时间不足60分钟付费取消
     * @param recyclerWithdrawForm
     * @return
     */
    Boolean chargeCancellationIfWithinOneHour(RecyclerWithdrawForm recyclerWithdrawForm);

    /**
     * 回收员拒收订单得到补偿
     * @param recyclerWithdrawForm
     * @return
     */
    Boolean rejectCompensate(RecyclerWithdrawForm recyclerWithdrawForm);
}
