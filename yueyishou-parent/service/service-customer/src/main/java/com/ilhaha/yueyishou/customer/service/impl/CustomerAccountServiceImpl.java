package com.ilhaha.yueyishou.customer.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ilhaha.yueyishou.model.entity.customer.CustomerAccount;
import com.ilhaha.yueyishou.customer.mapper.CustomerAccountMapper;
import com.ilhaha.yueyishou.customer.service.ICustomerAccountService;
import org.springframework.stereotype.Service;

@Service
public class CustomerAccountServiceImpl extends ServiceImpl<CustomerAccountMapper, CustomerAccount> implements ICustomerAccountService {

}
