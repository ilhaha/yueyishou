package com.ilhaha.yueyishou.recycler.client;

import com.ilhaha.yueyishou.common.result.Result;
import com.ilhaha.yueyishou.model.form.recycler.RecyclerAccountForm;
import com.ilhaha.yueyishou.model.form.recycler.RecyclerWithdrawForm;
import com.ilhaha.yueyishou.model.vo.recycler.RecyclerAccountVo;
import jakarta.annotation.Resource;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * @Author ilhaha
 * @Create 2024/9/2 20:11
 * @Version 1.0
 */
@FeignClient("service-recycler")

public interface RecyclerAccountFeignClient {

    /**
     * 回收员拒收订单得到补偿
     * @param recyclerWithdrawForm
     * @return
     */
    @PostMapping("/recyclerAccount/reject/compensate")
    Result<Boolean> rejectCompensate(@RequestBody RecyclerWithdrawForm recyclerWithdrawForm);

    /**
     * 回收员距离预约时间不足60分钟付费取消
     * @param recyclerWithdrawForm
     * @return
     */
    @PostMapping("/recyclerAccount/chargeCancellation")
    Result<Boolean> chargeCancellationIfWithinOneHour(@RequestBody RecyclerWithdrawForm recyclerWithdrawForm);

    /**
     * 顾客取消已超过免费时限，需支付相关费用取消订单
     * @param recyclerWithdrawForm
     * @return
     */
    @PostMapping("/recyclerAccount/processPaidCancellation")
    Result<Boolean> processPaidCancellation(@RequestBody RecyclerWithdrawForm recyclerWithdrawForm);


    /**
     * 超过预约时间未到达，需回收员赔偿取消
     * @param recyclerWithdrawForm
     * @return
     */
    @PostMapping("/recyclerAccount/cancelOrderIfOverdue")
    Result<Boolean> cancelOrderIfOverdue(@RequestBody RecyclerWithdrawForm recyclerWithdrawForm);

    /**
     * 结算订单
     * @param recyclerWithdrawForm
     * @return
     */
    @PostMapping("/recyclerAccount/settlement")
    Result<Boolean> settlement(@RequestBody RecyclerWithdrawForm recyclerWithdrawForm);

    /**
     * 回收员提现到微信零钱
     * @param recyclerWithdrawForm
     * @return
     */
    @PostMapping("/recyclerAccount/onWithdraw")
    Result<Boolean> onWithdraw(@RequestBody RecyclerWithdrawForm recyclerWithdrawForm);


    /**
     * 回收员给账号充值
     * @param recyclerWithdrawForm
     * @return
     */
    @PostMapping("/recyclerAccount/onRecharge")
    Result<Boolean> onRecharge(@RequestBody RecyclerWithdrawForm recyclerWithdrawForm);

    /**
     * 获取回收员账户信息
     * @param recyclerAccountForm
     * @return
     */
    @Resource
    @PostMapping("/recyclerAccount/info")
    Result<RecyclerAccountVo> getRecyclerAccountInfo(@RequestBody RecyclerAccountForm recyclerAccountForm);
}
