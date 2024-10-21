package com.ilhaha.yueyishou.customer.controller;

import com.ilhaha.yueyishou.common.anno.LoginVerification;
import com.ilhaha.yueyishou.common.result.Result;
import com.ilhaha.yueyishou.customer.service.BillService;
import com.ilhaha.yueyishou.model.form.order.UpdateBillForm;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author ilhaha
 * @Create 2024/10/19 17:50
 * @Version 1.0
 */
@RestController
@RequestMapping("/bill")
public class BillController {

    @Resource
    private BillService billService;

    /**
     * 修改账单信息
     * @param updateBillForm
     * @return
     */
    @LoginVerification
    @PostMapping("/update")
    public Result<String> updateBill(@RequestBody UpdateBillForm updateBillForm){
        return billService.updateBill(updateBillForm);
    }
}
