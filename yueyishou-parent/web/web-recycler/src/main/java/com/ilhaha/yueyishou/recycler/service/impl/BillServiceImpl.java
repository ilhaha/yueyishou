package com.ilhaha.yueyishou.recycler.service.impl;

import com.ilhaha.yueyishou.common.result.Result;
import com.ilhaha.yueyishou.model.form.order.GenerateBillForm;
import com.ilhaha.yueyishou.order.client.OrderBillFeignClient;
import com.ilhaha.yueyishou.recycler.service.BillService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * @Author ilhaha
 * @Create 2024/10/19 12:22
 * @Version 1.0
 */
@Service
public class BillServiceImpl implements BillService {

    @Resource
    private OrderBillFeignClient orderBillFeignClient;

    /**
     * 生成订单账单
     * @param generateBillForm
     * @return
     */
    @Override
    public Result<Boolean> generateOrder(GenerateBillForm generateBillForm) {
        return orderBillFeignClient.generateOrder(generateBillForm);
    }
}
