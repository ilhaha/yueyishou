package com.ilhaha.yueyishou.customer.client;

import com.ilhaha.yueyishou.common.result.Result;
import com.ilhaha.yueyishou.model.entity.customer.CustomerAddress;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author ilhaha
 * @Create 2024/9/22 21:40
 * @Version 1.0
 */
@FeignClient("service-customer")
public interface CustomerAddressFeignClient {
    /**
     * 获取当前登录的顾客地址列表
     * @return
     */
    @GetMapping(value = "/customerAddress/list/{customerId}")
    Result<List<CustomerAddress>> getAddressList(@PathVariable("customerId") Long customerId);

    /**
     * 通过id查询地址信息
     *
     * @param addressId
     * @return
     */
    @GetMapping(value = "/customerAddress/{addressId}")
    Result<CustomerAddress> getAddressById(@PathVariable("addressId") Integer addressId);

    /**
     *   添加地址
     *
     * @param customerAddress
     * @return
     */
    @PostMapping(value = "/customerAddress/add")
    Result<String> add(@RequestBody CustomerAddress customerAddress);

    /**
     *  编辑地址信息
     *
     * @param customerAddress
     * @return
     */
    @PostMapping(value = "/customerAddress/edit")
    Result<String> edit(@RequestBody CustomerAddress customerAddress);

    /**
     *   通过id删除
     *
     * @param id
     * @return
     */
    @PostMapping(value = "/customerAddress/delete/{id}")
    Result<String> delete(@PathVariable("id") Integer id);

    /**
     * 获取当前顾客的默认地址
     * @param customerId
     * @return
     */
    @GetMapping("/customerAddress/default/{customerId}")
    Result<CustomerAddress> getDefaultAddress(@PathVariable("customerId") Long customerId);

}
