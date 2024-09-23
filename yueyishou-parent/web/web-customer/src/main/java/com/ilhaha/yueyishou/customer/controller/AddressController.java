package com.ilhaha.yueyishou.customer.controller;

import com.ilhaha.yueyishou.common.anno.LoginVerification;
import com.ilhaha.yueyishou.common.result.Result;
import com.ilhaha.yueyishou.customer.service.AddressService;
import com.ilhaha.yueyishou.model.entity.customer.CustomerAddress;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author ilhaha
 * @Create 2024/9/22 21:41
 * @Version 1.0
 */
@RestController
@RequestMapping("/address")
public class AddressController {

    @Resource
    private AddressService addressService;

    /**
     * 获取当前登录的顾客地址列表
     * @return
     */
    @LoginVerification
    @GetMapping(value = "/list")
    public Result<List<CustomerAddress>> getAddressList() {
        return addressService.getAddressList();
    }

    /**
     * 通过id查询地址信息
     *
     * @param addressId
     * @return
     */
    @LoginVerification
    @GetMapping(value = "/{addressId}")
    public Result<CustomerAddress> getAddressById(@PathVariable("addressId") Integer addressId) {
        return addressService.getAddressById(addressId);
    }

    /**
     *   添加地址
     *
     * @param customerAddress
     * @return
     */
    @LoginVerification
    @PostMapping(value = "/add")
    public Result<String> add(@RequestBody CustomerAddress customerAddress) {
        System.out.println(customerAddress);
        return addressService.addAddress(customerAddress);
    }

    /**
     *  编辑地址信息
     *
     * @param customerAddress
     * @return
     */
    @LoginVerification
    @PostMapping(value = "/edit")
    public Result<String> edit(@RequestBody CustomerAddress customerAddress) {
        System.out.println(customerAddress);
        return addressService.updateAddress(customerAddress);
    }

    /**
     *   通过id删除
     *
     * @param id
     * @return
     */
    @PostMapping(value = "/delete/{id}")
    public Result<String> delete(@PathVariable("id") Integer id) {
        return addressService.removeById(id);
    }

    /**
     * 获取当前顾客的默认地址
     * @return
     */
    @LoginVerification
    @GetMapping("/default")
    public Result<CustomerAddress> getDefaultAddress(){
        return addressService.getDefaultAddress();
    }
}
