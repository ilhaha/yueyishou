package com.ilhaha.yueyishou.customer.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ilhaha.yueyishou.model.entity.customer.CustomerInfo;
import com.ilhaha.yueyishou.model.form.customer.UpdateCustomerStatusForm;
import com.ilhaha.yueyishou.model.form.recycler.RecyclerApplyForm;
import com.ilhaha.yueyishou.model.vo.customer.CustomerLoginInfoVo;

public interface ICustomerInfoService extends IService<CustomerInfo> {

    /**
     * 小程序授权登录
     * @param code
     * @return
     */
    String login(String code);

    /**
     * 修改客户状态
     * @param updateCustomerStatusForm
     * @return
     */
    String switchStatus(UpdateCustomerStatusForm updateCustomerStatusForm);

    /**
     * 分页查询
     * @param page
     * @param customerInfo
     * @return
     */
    Page<CustomerInfo> queryPageList(Page<CustomerInfo> page, CustomerInfo customerInfo);

    /**
     * 获取顾客登录之后的顾客信息
     * @param customerId
     * @return
     */
    CustomerLoginInfoVo getLoginInfo(Long customerId);

    /**
     * 认证成为回收员
     * @param recyclerApplyForm
     * @return
     */
    Boolean authRecycler(RecyclerApplyForm recyclerApplyForm);

}
