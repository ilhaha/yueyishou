package com.ilhaha.yueyishou.customer.client;

import com.ilhaha.yueyishou.common.result.Result;
import com.ilhaha.yueyishou.model.form.customer.CustomerAccountForm;
import com.ilhaha.yueyishou.model.form.customer.CustomerWithdrawForm;
import com.ilhaha.yueyishou.model.form.recycler.RecyclerWithdrawForm;
import com.ilhaha.yueyishou.model.vo.customer.CustomerAccountVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @Author ilhaha
 * @Create 2024/9/2 16:06
 * @Version 1.0
 */
@FeignClient("service-customer")
public interface CustomerAccountFeignClient {

    /**
     * 回收员距离预约时间不足60分钟付费取消
     * @param customerWithdrawForm
     * @return
     */
    @PostMapping("/customerAccount/chargeCancellation")
    Result<Boolean> chargeCancellationIfWithinOneHour(@RequestBody CustomerWithdrawForm customerWithdrawForm);

    /**
     * 顾客取消已超过免费时限，需支付相关费用取消订单
     * @param customerWithdrawForm
     * @return
     */
    @PostMapping("/customerAccount/processPaidCancellation")
    Result<Boolean> processPaidCancellation(@RequestBody CustomerWithdrawForm customerWithdrawForm);

    /**
     * 超过预约时间未到达，需回收员赔偿取消
     * @param customerWithdrawForm
     * @return
     */
    @PostMapping("/customerAccount/cancelOrderIfOverdue")
    Result<Boolean> cancelOrderIfOverdue(@RequestBody CustomerWithdrawForm customerWithdrawForm);


    /**
     * 顾客提现到微信零钱
     * @param customerWithdrawForm
     * @return
     */
    @PostMapping("/customerAccount/onWithdraw")
    Result<Boolean> onWithdraw(@RequestBody CustomerWithdrawForm customerWithdrawForm);

    /**
     * 获取顾客账户信息
     * @param customerAccountForm
     * @return
     */
    @PostMapping("/customerAccount/info")
    Result<CustomerAccountVo> getCustomerAccountInfo(@RequestBody CustomerAccountForm customerAccountForm);

    /**
     * 结算顾客订单
     * @param customerWithdrawForm
     * @return
     */
    @PostMapping("/customerAccount/settlement")
    Result<Boolean> settlement(@RequestBody CustomerWithdrawForm customerWithdrawForm);
}
