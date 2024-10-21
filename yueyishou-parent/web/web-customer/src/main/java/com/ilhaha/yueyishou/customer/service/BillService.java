package com.ilhaha.yueyishou.customer.service;

import com.ilhaha.yueyishou.common.result.Result;
import com.ilhaha.yueyishou.model.form.order.UpdateBillForm;

/**
 * @Author ilhaha
 * @Create 2024/10/19 17:50
 * @Version 1.0
 */
public interface BillService {

    /**
     * 修改账单信息
     * @param updateBillForm
     * @return
     */
    Result<String> updateBill(UpdateBillForm updateBillForm);
}
