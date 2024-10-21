package com.ilhaha.yueyishou.order.client;

import com.ilhaha.yueyishou.common.result.Result;
import com.ilhaha.yueyishou.model.form.order.GenerateBillForm;
import com.ilhaha.yueyishou.model.form.order.UpdateBillForm;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @Author ilhaha
 * @Create 2024/10/19 12:21
 * @Version 1.0
 */
@FeignClient("service-order")
public interface OrderBillFeignClient {

    /**
     * 生成订单账单
     *
     * @param generateBillForm
     * @return
     */
    @PostMapping("/orderBill/generate")
    Result<Boolean> generateOrder(@RequestBody GenerateBillForm generateBillForm);

    /**
     * 修改账单信息
     * @param updateBillForm
     * @return
     */
    @PostMapping("/orderBill/update")
    Result<String> updateBill(@RequestBody UpdateBillForm updateBillForm);
}
