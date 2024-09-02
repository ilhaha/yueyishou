package com.ilhaha.yueyishou.customer.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ilhaha.yueyishou.entity.customer.CustomerInfo;
import com.ilhaha.yueyishou.customer.mapper.CustomerInfoMapper;
import com.ilhaha.yueyishou.customer.service.ICustomerInfoService;
import org.springframework.stereotype.Service;

@Service
public class CustomerInfoServiceImpl extends ServiceImpl<CustomerInfoMapper, CustomerInfo> implements ICustomerInfoService {

}
