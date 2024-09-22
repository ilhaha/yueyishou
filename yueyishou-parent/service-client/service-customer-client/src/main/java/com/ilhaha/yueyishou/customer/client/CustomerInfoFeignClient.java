package com.ilhaha.yueyishou.customer.client;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ilhaha.yueyishou.model.entity.customer.CustomerInfo;
import com.ilhaha.yueyishou.model.form.customer.UpdateCustomerStatusForm;
import com.ilhaha.yueyishou.common.result.Result;
import com.ilhaha.yueyishou.model.vo.customer.CustomerLoginInfoVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * @Author ilhaha
 * @Create 2024/9/2 16:05
 * @Version 1.0
 */
@FeignClient("service-customer")
public interface CustomerInfoFeignClient {

    /**
     * 获取顾客登录之后的顾客信息
     * @param customerId
     * @return
     */
    @GetMapping("/customerInfo/login/info/{customerId}")
    Result<CustomerLoginInfoVo> getLoginInfo(@PathVariable("customerId") Long customerId);

    /**
     * 修改客户状态
     * @param updateCustomerStatusForm
     * @return
     */
    @PostMapping("/customerInfo/switch/status")
    Result<String> switchStatus(@RequestBody UpdateCustomerStatusForm updateCustomerStatusForm);

    /**
     * 小程序授权登录
     * @param code
     * @return
     */
    @GetMapping("/customerInfo/login/{code}")
    Result<String> login(@PathVariable("code") String code);

    /**
     * 客户分页列表查询
     *
     * @param customerInfo
     * @param pageNo
     * @param pageSize
     * @return
     */
    @PostMapping("/customerInfo/list")
    Result<Page<CustomerInfo>> queryPageList(@RequestBody CustomerInfo customerInfo,
                                                    @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                    @RequestParam(name="pageSize", defaultValue="10") Integer pageSize);
}
