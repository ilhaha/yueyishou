package com.ilhaha.yueyishou.customer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ilhaha.yueyishou.customer.mapper.CustomerAccountOperateMapper;
import com.ilhaha.yueyishou.customer.service.ICustomerAccountOperateService;
import com.ilhaha.yueyishou.model.entity.customer.CustomerAccountOperate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author ilhaha
 * @Create 2024/11/18 11:30
 * @Version 1.0
 */
@Service
public class CustomerAccountOperateServiceImpl extends ServiceImpl<CustomerAccountOperateMapper,CustomerAccountOperate> implements ICustomerAccountOperateService {

    /**
     * 添加操作顾客账户记录
     * @param customerAccountOperate
     * @return
     */
    @Override
    public Boolean add(CustomerAccountOperate customerAccountOperate) {
        return this.save(customerAccountOperate);
    }

    /**
     * 获取顾客账号状态操作日志
     *
     * @param page
     * @param customerId
     * @return
     */
    @Override
    public  Page<CustomerAccountOperate> getOperateList(Page<CustomerAccountOperate> page, Long customerId) {
        LambdaQueryWrapper<CustomerAccountOperate> customerAccountOperateLambdaQueryWrapper = new LambdaQueryWrapper<>();
        customerAccountOperateLambdaQueryWrapper.eq(CustomerAccountOperate::getCustomerId,customerId)
                .orderByDesc(CustomerAccountOperate::getCreateTime);
        return this.page(page,customerAccountOperateLambdaQueryWrapper);
    }
}
