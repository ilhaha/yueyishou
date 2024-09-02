package com.ilhaha.yueyishou.customer.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ilhaha.yueyishou.entity.customer.CustomerAccountDetail;
import com.ilhaha.yueyishou.customer.mapper.CustomerAccountDetailMapper;
import com.ilhaha.yueyishou.customer.service.ICustomerAccountDetailService;
import org.springframework.stereotype.Service;

@Service
public class CustomerAccountDetailServiceImpl extends ServiceImpl<CustomerAccountDetailMapper, CustomerAccountDetail> implements ICustomerAccountDetailService {

}
