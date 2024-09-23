package com.ilhaha.yueyishou.customer.controller;


import com.ilhaha.yueyishou.common.result.Result;
import com.ilhaha.yueyishou.customer.service.ICustomerAddressService;
import com.ilhaha.yueyishou.model.entity.customer.CustomerAddress;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;


@RestController
@RequestMapping("/customerAddress")
public class CustomerAddressController {
	@Resource
	private ICustomerAddressService customerAddressService;

	/**
	 * 获取当前顾客的默认地址
	 * @param customerId
	 * @return
	 */
	@GetMapping("/default/{customerId}")
	public Result<CustomerAddress> getDefaultAddress(@PathVariable("customerId") Long customerId){
		return Result.ok(customerAddressService.getDefaultAddress(customerId));
	}

	/**
	 * 获取当前登录的顾客地址列表
	 * @return
	 */
	@GetMapping(value = "/list/{customerId}")
	public Result<List<CustomerAddress>> getAddressList(@PathVariable("customerId") Long customerId) {
		return Result.ok(customerAddressService.getAddressList(customerId));
	}
	
	/**
	 *   添加地址
	 *
	 * @param customerAddress
	 * @return
	 */
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody CustomerAddress customerAddress) {
		customerAddressService.addAddress(customerAddress);
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑地址信息
	 *
	 * @param customerAddress
	 * @return
	 */
	@PostMapping(value = "/edit")
	public Result<String> edit(@RequestBody CustomerAddress customerAddress) {
		customerAddressService.updateAddress(customerAddress);
		return Result.ok("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@PostMapping(value = "/delete/{id}")
	public Result<String> delete(@PathVariable("id") Integer id) {
		customerAddressService.removeById(id);
		return Result.ok("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.customerAddressService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功!");
	}
	
	/**
	 * 通过id查询地址信息
	 *
	 * @param addressId
	 * @return
	 */
	@GetMapping(value = "/{addressId}")
	public Result<CustomerAddress> getAddressById(@PathVariable("addressId") Integer addressId) {
		return Result.ok(customerAddressService.getAddressById(addressId));
	}

}
