package com.ilhaha.yueyishou.customer.service;

import com.ilhaha.yueyishou.common.result.Result;
import com.ilhaha.yueyishou.model.entity.customer.CustomerAddress;

import java.util.List;

/**
 * @Author ilhaha
 * @Create 2024/9/22 21:42
 * @Version 1.0
 */
public interface AddressService {
    /**
     * 获取当前登录的顾客地址列表
     * @return
     */
    Result<List<CustomerAddress>> getAddressList();

    /**
     * 通过id查询地址信息
     *
     * @param addressId
     * @return
     */
    Result<CustomerAddress> getAddressById(Integer addressId);

    /**
     *   添加地址
     *
     * @param customerAddress
     * @return
     */
    Result<String> addAddress(CustomerAddress customerAddress);

    /**
     *  编辑地址信息
     *
     * @param customerAddress
     * @return
     */
    Result<String> updateAddress(CustomerAddress customerAddress);

    /**
     * 根据id删除地址信息
     * @param id
     * @return
     */
    Result<String> removeById(Integer id);

    /**
     * 获取当前顾客的默认地址
     * @return
     */
    Result<CustomerAddress> getDefaultAddress();
}
