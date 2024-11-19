package org.jeecg.modules.mgr.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ilhaha.yueyishou.model.entity.customer.CustomerAccountOperate;
import com.ilhaha.yueyishou.model.entity.customer.CustomerInfo;
import com.ilhaha.yueyishou.model.form.customer.UpdateCustomerStatusForm;
import com.ilhaha.yueyishou.model.vo.customer.ExamineInfoVo;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.mgr.service.CustomerService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

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
     * 获取顾客账号状态操作日志
     *
     * @param customerId
     * @return
     */
    @GetMapping("/account/list")
    public Result<Page<CustomerAccountOperate>> getOperateList(@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                               @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                               @RequestParam("customerId") Long customerId) {
        return Result.OK(customerService.getOperateList(pageNo,pageSize,customerId));
    }


    /**
     * 添加操作顾客账户记录
     *
     * @param customerAccountOperate
     * @return
     */
    @PostMapping("/account/operate/add")
    public Result<Boolean> add(@RequestBody CustomerAccountOperate customerAccountOperate) {
        return Result.OK(customerService.add(customerAccountOperate));
    }

    /**
     * 修改客户状态
     *
     * @param updateCustomerStatusForm
     * @return
     */
    @PostMapping("/switch/status")
    public Result<String> switchStatus(@RequestBody UpdateCustomerStatusForm updateCustomerStatusForm) {
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
                                             @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                             @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        return Result.OK(customerService.queryPageList(customerInfo, pageNo, pageSize));
    }
}
