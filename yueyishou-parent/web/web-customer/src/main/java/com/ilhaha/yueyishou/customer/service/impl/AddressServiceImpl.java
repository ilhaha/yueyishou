package com.ilhaha.yueyishou.customer.service.impl;

import com.ilhaha.yueyishou.common.result.Result;
import com.ilhaha.yueyishou.common.util.AuthContextHolder;
import com.ilhaha.yueyishou.customer.client.CustomerAddressFeignClient;
import com.ilhaha.yueyishou.customer.service.AddressService;
import com.ilhaha.yueyishou.model.entity.customer.CustomerAddress;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author ilhaha
 * @Create 2024/9/22 21:42
 * @Version 1.0
 */
@Service
public class AddressServiceImpl implements AddressService {

    @Resource
    private CustomerAddressFeignClient customerAddressFeignClient;

    /**
     * 获取当前登录的顾客地址列表
     * @return
     */
    @Override
    public Result<List<CustomerAddress>> getAddressList() {
        Long customerId = AuthContextHolder.getCustomerId();
        return customerAddressFeignClient.getAddressList(customerId);
    }

    /**
     * 通过id查询地址信息
     *
     * @param addressId
     * @return
     */
    @Override
    public Result<CustomerAddress> getAddressById(Integer addressId) {
        return customerAddressFeignClient.getAddressById(addressId);
    }

    /**
     * 新增新地址
     * @param customerAddress
     * @return
     */
    @Override
    public Result<String> addAddress(CustomerAddress customerAddress) {
        customerAddress.setCustomerId(AuthContextHolder.getCustomerId());
        return customerAddressFeignClient.add(customerAddress);
    }

    /**
     * 修改地址
     * @param customerAddress
     * @return
     */
    @Override
    public Result<String> updateAddress(CustomerAddress customerAddress) {
        customerAddress.setCustomerId(AuthContextHolder.getCustomerId());
        return customerAddressFeignClient.edit(customerAddress);
    }

    /**
     * 根据id删除地址信息
     * @param id
     * @return
     */
    @Override
    public Result<String> removeById(Integer id) {
        return customerAddressFeignClient.delete(id);
    }

    /**
     * 获取当前顾客的默认地址
     * @return
     */
    @Override
    public Result<CustomerAddress> getDefaultAddress() {
        return customerAddressFeignClient.getDefaultAddress(AuthContextHolder.getCustomerId());
    }
}
