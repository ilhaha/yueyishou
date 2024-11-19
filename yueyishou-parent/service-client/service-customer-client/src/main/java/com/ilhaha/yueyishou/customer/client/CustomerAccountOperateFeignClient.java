package com.ilhaha.yueyishou.customer.client;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ilhaha.yueyishou.common.result.Result;
import com.ilhaha.yueyishou.model.entity.customer.CustomerAccountOperate;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @Author ilhaha
 * @Create 2024/11/18 11:45
 * @Version 1.0
 */
@FeignClient("service-customer")

public interface CustomerAccountOperateFeignClient {

    /**
     * 获取顾客账号状态操作日志
     * @param customerId
     * @return
     */
    @GetMapping("/customerAccountOperate/list")
    Result<Page<CustomerAccountOperate>> getOperateList(@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                               @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                               @RequestParam("customerId") Long customerId);

    /**
     * 添加操作记录
     * @param customerAccountOperate
     * @return
     */
    @PostMapping("/customerAccountOperate/add")
    Result<Boolean> add(@RequestBody CustomerAccountOperate customerAccountOperate);
}
