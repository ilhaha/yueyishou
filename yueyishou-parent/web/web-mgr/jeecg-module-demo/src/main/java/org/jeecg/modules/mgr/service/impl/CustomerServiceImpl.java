package org.jeecg.modules.mgr.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ilhaha.yueyishou.customer.client.CustomerInfoFeignClient;
import com.ilhaha.yueyishou.model.entity.customer.CustomerInfo;
import com.ilhaha.yueyishou.model.form.customer.UpdateCustomerStatusForm;
import org.jeecg.modules.mgr.service.CustomerService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author ilhaha
 * @Create 2024/9/7 21:52
 * @Version 1.0
 */
@Service
public class CustomerServiceImpl implements CustomerService {

    @Resource
    private CustomerInfoFeignClient customerInfoFeignClient;

    /**
     * 客户分页列表查询
     *
     * @param customerInfo
     * @param pageNo
     * @param pageSize
     * @return
     */
    @Override
    public Page<CustomerInfo> queryPageList(CustomerInfo customerInfo, Integer pageNo, Integer pageSize) {
        return customerInfoFeignClient.queryPageList(customerInfo,pageNo,pageSize).getData();
    }

    /**
     * 修改客户状态
     * @param updateCustomerStatusForm
     * @return
     */
    @Override
    public String switchStatus(UpdateCustomerStatusForm updateCustomerStatusForm) {
        return customerInfoFeignClient.switchStatus(updateCustomerStatusForm).getData();
    }
}
