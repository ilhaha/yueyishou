package com.ilhaha.yueyishou.recycler.controller;

import com.ilhaha.yueyishou.common.result.Result;
import com.ilhaha.yueyishou.model.form.order.GenerateBillForm;
import com.ilhaha.yueyishou.recycler.service.BillService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author ilhaha
 * @Create 2024/10/19 12:22
 * @Version 1.0
 */
@RestController
@RequestMapping("/bill")
public class BillController {

    @Resource
    private BillService billService;

    /**
     * 生成订单账单
     * @param generateBillForm
     * @return
     */
    @PostMapping("/generate")
    public Result<Boolean> generateOrder(@RequestBody GenerateBillForm generateBillForm){
        return billService.generateOrder(generateBillForm);
    }

}
