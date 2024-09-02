package com.ilhaha.yueyishou.customer.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ilhaha.yueyishou.entity.customer.CustomerLoginLog;
import com.ilhaha.yueyishou.customer.mapper.CustomerLoginLogMapper;
import com.ilhaha.yueyishou.customer.service.ICustomerLoginLogService;
import org.springframework.stereotype.Service;

@Service
public class CustomerLoginLogServiceImpl extends ServiceImpl<CustomerLoginLogMapper, CustomerLoginLog> implements ICustomerLoginLogService {

}
