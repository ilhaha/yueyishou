package com.ilhaha.yueyishou.customer.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ilhaha.yueyishou.entity.customer.CustomerInfo;
import com.ilhaha.yueyishou.customer.service.ICustomerInfoService;
import com.ilhaha.yueyishou.execption.YueYiShouException;
import com.ilhaha.yueyishou.result.Result;
import com.ilhaha.yueyishou.result.ResultCodeEnum;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@RestController
@RequestMapping("/customerInfo")
@Slf4j
public class CustomerInfoController {
	@Autowired
	private ICustomerInfoService customerInfoService;


	
	/**
	 * 分页列表查询
	 *
	 * @param customerInfo
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	public Result<IPage<CustomerInfo>> queryPageList(CustomerInfo customerInfo,
													 @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
													 @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
													 HttpServletRequest req) {
		LambdaQueryWrapper<CustomerInfo> customerInfoLambdaQueryWrapper = new LambdaQueryWrapper<>();
		Page<CustomerInfo> page = new Page<CustomerInfo>(pageNo, pageSize);
		IPage<CustomerInfo> pageList = customerInfoService.page(page, customerInfoLambdaQueryWrapper);
		return Result.ok(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param customerInfo
	 * @return
	 */
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody CustomerInfo customerInfo) {
		customerInfoService.save(customerInfo);
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param customerInfo
	 * @return
	 */
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody CustomerInfo customerInfo) {
		customerInfoService.updateById(customerInfo);
		return Result.ok("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		customerInfoService.removeById(id);
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
		this.customerInfoService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@GetMapping(value = "/queryById")
	public Result<CustomerInfo> queryById(@RequestParam(name="id",required=true) String id) {
		CustomerInfo customerInfo = customerInfoService.getById(id);
		if(customerInfo==null) {
			throw new YueYiShouException(ResultCodeEnum.DATA_ERROR);
		}
		return Result.ok(customerInfo);
	}

}
