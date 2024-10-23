package com.ilhaha.yueyishou.customer.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ilhaha.yueyishou.model.entity.customer.CustomerAccountDetail;
import com.ilhaha.yueyishou.model.form.recycler.AddDetailsForm;
import com.ilhaha.yueyishou.model.vo.customer.CustomerAccountDetailVo;

import java.util.List;

public interface ICustomerAccountDetailService extends IService<CustomerAccountDetail> {

    /**
     * 增加明细
     * @param addDetailsForm
     */
    void addDetails(AddDetailsForm addDetailsForm);

    /**
     * 获取账户明细信息
     * @param accountId
     * @param detailsTime
     * @return
     */
    List<CustomerAccountDetailVo> getCustomerAccountDetail(Long accountId, String detailsTime);
}
