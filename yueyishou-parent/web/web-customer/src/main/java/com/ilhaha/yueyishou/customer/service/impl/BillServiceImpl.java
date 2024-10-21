package com.ilhaha.yueyishou.customer.service.impl;

import com.ilhaha.yueyishou.common.result.Result;
import com.ilhaha.yueyishou.common.util.AuthContextHolder;
import com.ilhaha.yueyishou.customer.service.BillService;
import com.ilhaha.yueyishou.model.form.order.UpdateBillForm;
import com.ilhaha.yueyishou.order.client.OrderBillFeignClient;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * @Author ilhaha
 * @Create 2024/10/19 17:50
 * @Version 1.0
 */
@Service
public class BillServiceImpl implements BillService {

    @Resource
    private OrderBillFeignClient orderBillFeignClient;

    /**
     * 修改账单信息
     * @param updateBillForm
     * @return
     */
    @Override
    public Result<String> updateBill(UpdateBillForm updateBillForm) {
        updateBillForm.setCustomerId(AuthContextHolder.getCustomerId());
        return orderBillFeignClient.updateBill(updateBillForm);
    }
}
