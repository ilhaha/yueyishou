package com.ilhaha.yueyishou.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ilhaha.yueyishou.model.entity.order.OrderBill;
import com.ilhaha.yueyishou.model.form.order.GenerateBillForm;

public interface IOrderBillService extends IService<OrderBill> {

    /**
     * 生成订单账单
     * @param generateBillForm
     * @return
     */
    Boolean generateOrder(GenerateBillForm generateBillForm);
}
