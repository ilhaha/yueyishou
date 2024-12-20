package com.ilhaha.yueyishou.customer.client;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ilhaha.yueyishou.model.entity.customer.CustomerInfo;
import com.ilhaha.yueyishou.model.form.customer.UpdateCustomerBaseInfoForm;
import com.ilhaha.yueyishou.model.form.customer.UpdateCustomerStatusForm;
import com.ilhaha.yueyishou.common.result.Result;
import com.ilhaha.yueyishou.model.form.recycler.RecyclerApplyForm;
import com.ilhaha.yueyishou.model.vo.customer.CustomerLoginInfoVo;
import com.ilhaha.yueyishou.model.vo.customer.CustomerMyVo;
import com.ilhaha.yueyishou.model.vo.customer.ExamineInfoVo;
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
     * 获取顾客认证回收员的审核反馈信息
     * @param customerId
     * @return
     */
    @GetMapping("/customerInfo/audit/feedback/{customerId}")
    Result<ExamineInfoVo> auditFeedback(@PathVariable("customerId") Long customerId);

    /**
     * 获取顾客我的页面初始信息
     * @param customerId
     * @return
     */
    @GetMapping("/customerInfo/my/{customerId}")
    Result<CustomerMyVo> getMy(@PathVariable Long customerId);

    /**
     * 更新顾客基本信息
     * @param updateCustomerBaseInfoForm
     * @return
     */
    @PostMapping("/customerInfo/update/base/info")
    Result<Boolean> updateBaseInfo(@RequestBody UpdateCustomerBaseInfoForm updateCustomerBaseInfoForm);

    /**
     * 认证成为回收员
     * @param recyclerApplyForm
     * @return
     */
    @PostMapping("/customerInfo/auth/recycler")
    Result<Boolean> authRecycler(@RequestBody RecyclerApplyForm recyclerApplyForm);

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


    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/customerInfo/queryById")
    Result<CustomerInfo> queryById(@RequestParam(name="id",required=true) Long id);
}
