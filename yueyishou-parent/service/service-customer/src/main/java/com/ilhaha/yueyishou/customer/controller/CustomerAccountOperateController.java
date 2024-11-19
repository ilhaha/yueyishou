package com.ilhaha.yueyishou.customer.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ilhaha.yueyishou.common.result.Result;
import com.ilhaha.yueyishou.customer.service.ICustomerAccountOperateService;
import com.ilhaha.yueyishou.model.entity.customer.CustomerAccountOperate;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author ilhaha
 * @Create 2024/11/18 11:29
 * @Version 1.0
 */
@RestController
@RequestMapping("/customerAccountOperate")
public class CustomerAccountOperateController {

    @Resource
    private ICustomerAccountOperateService customerAccountOperateService;

    /**
     * 获取顾客账号状态操作日志
     * @param customerId
     * @return
     */
    @GetMapping("/list")
    public Result<Page<CustomerAccountOperate>> getOperateList(@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                               @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                               @RequestParam("customerId") Long customerId) {
        Page<CustomerAccountOperate> page = new Page<>(pageNo, pageSize);
        return Result.ok(customerAccountOperateService.getOperateList(page,customerId));
    }

    /**
     * 添加操作顾客账户记录
     * @param customerAccountOperate
     * @return
     */
    @PostMapping("/add")
    public Result<Boolean> add(@RequestBody CustomerAccountOperate customerAccountOperate){
        return Result.ok(customerAccountOperateService.add(customerAccountOperate));
    }
}
