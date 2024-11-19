package com.ilhaha.yueyishou.customer.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ilhaha.yueyishou.model.entity.customer.CustomerAccountOperate;

import java.util.List;

/**
 * @Author ilhaha
 * @Create 2024/11/18 11:30
 * @Version 1.0
 */
public interface ICustomerAccountOperateService extends IService<CustomerAccountOperate> {

    /**
     * 添加操作顾客账户记录
     * @param customerAccountOperate
     * @return
     */
    Boolean add(CustomerAccountOperate customerAccountOperate);

    /**
     * 获取顾客账号状态操作日志
     *
     * @param page
     * @param customerId
     * @return
     */
    Page<CustomerAccountOperate> getOperateList(Page<CustomerAccountOperate> page, Long customerId);
}
