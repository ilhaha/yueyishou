package org.jeecg.modules.mgr.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ilhaha.yueyishou.model.entity.customer.CustomerInfo;
import com.ilhaha.yueyishou.model.form.customer.UpdateCustomerStatusForm;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.mgr.service.CustomerService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @Author ilhaha
 * @Create 2024/9/7 21:51
 * @Version 1.0
 */
@RequestMapping("/customer")
@RestController
public class CustomerController {

    @Resource
    private CustomerService customerService;

    /**
     * 修改客户状态
     * @param updateCustomerStatusForm
     * @return
     */
    @PostMapping("/switch/status")
    public Result<String> switchStatus(@RequestBody UpdateCustomerStatusForm updateCustomerStatusForm){
        return Result.ok(customerService.switchStatus(updateCustomerStatusForm));
    }

    /**
     * 客户分页列表查询
     *
     * @param customerInfo
     * @param pageNo
     * @param pageSize
     * @return
     */
    @GetMapping("/list")
    Result<Page<CustomerInfo>> queryPageList(CustomerInfo customerInfo,
                                             @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                             @RequestParam(name="pageSize", defaultValue="10") Integer pageSize){
        return Result.OK(customerService.queryPageList(customerInfo,pageNo,pageSize));
    }
}
