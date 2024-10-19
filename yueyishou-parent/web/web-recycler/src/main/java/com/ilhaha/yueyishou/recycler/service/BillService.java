package com.ilhaha.yueyishou.recycler.service;

import com.ilhaha.yueyishou.common.result.Result;
import com.ilhaha.yueyishou.model.form.order.GenerateBillForm;

/**
 * @Author ilhaha
 * @Create 2024/10/19 12:22
 * @Version 1.0
 */
public interface BillService {

    /**
     * 生成订单账单
     * @param generateBillForm
     * @return
     */
    Result<Boolean> generateOrder(GenerateBillForm generateBillForm);
}
