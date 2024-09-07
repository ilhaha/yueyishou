package com.ilhaha.yueyishou.customer.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ilhaha.yueyishou.entity.customer.CustomerInfo;
import com.ilhaha.yueyishou.form.customer.UpdateCustomerStatusForm;
import com.mysql.cj.log.Log;

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

}
