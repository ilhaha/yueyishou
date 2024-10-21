package com.ilhaha.yueyishou.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ilhaha.yueyishou.model.entity.order.OrderBill;
import com.ilhaha.yueyishou.model.form.order.GenerateBillForm;
import com.ilhaha.yueyishou.model.form.order.UpdateBillForm;

public interface IOrderBillService extends IService<OrderBill> {

    /**
     * 生成订单账单
     * @param generateBillForm
     * @return
     */
    Boolean generateOrder(GenerateBillForm generateBillForm);

    /**
     * 根据订单id获取订单账单信息
     * @param orderId
     * @return
     */
    OrderBill getBillInfoByOrderId(Long orderId);

    /**
     * 修改账单信息
     * @param updateBillForm
     * @return
     */
    String updateBill(UpdateBillForm updateBillForm);
}
