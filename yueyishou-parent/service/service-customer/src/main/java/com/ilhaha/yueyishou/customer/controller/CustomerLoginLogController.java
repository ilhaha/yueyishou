package com.ilhaha.yueyishou.customer.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ilhaha.yueyishou.entity.customer.CustomerLoginLog;
import com.ilhaha.yueyishou.customer.service.ICustomerLoginLogService;
import com.ilhaha.yueyishou.execption.YueYiShouException;
import com.ilhaha.yueyishou.result.Result;
import com.ilhaha.yueyishou.result.ResultCodeEnum;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@RestController
@RequestMapping("/customerLoginLog")
@Slf4j
public class CustomerLoginLogController {
	@Resource
	private ICustomerLoginLogService customerLoginLogService;
	
	/**
	 * 分页列表查询
	 *
	 * @param customerLoginLog
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/list")
	public Result<IPage<CustomerLoginLog>> queryPageList(CustomerLoginLog customerLoginLog,
														 @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
														 @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
														 HttpServletRequest req) {
		LambdaQueryWrapper<CustomerLoginLog> customerLoginLogLambdaQueryWrapper = new LambdaQueryWrapper<CustomerLoginLog>();
		Page<CustomerLoginLog> page = new Page<CustomerLoginLog>(pageNo, pageSize);
		IPage<CustomerLoginLog> pageList = customerLoginLogService.page(page, customerLoginLogLambdaQueryWrapper);
		return Result.ok(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param customerLoginLog
	 * @return
	 */
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody CustomerLoginLog customerLoginLog) {
		customerLoginLogService.save(customerLoginLog);
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param customerLoginLog
	 * @return
	 */
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody CustomerLoginLog customerLoginLog) {
		customerLoginLogService.updateById(customerLoginLog);
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
		customerLoginLogService.removeById(id);
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
		this.customerLoginLogService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@GetMapping(value = "/queryById")
	public Result<CustomerLoginLog> queryById(@RequestParam(name="id",required=true) String id) {
		CustomerLoginLog customerLoginLog = customerLoginLogService.getById(id);
		if(customerLoginLog==null) {
			throw new YueYiShouException(ResultCodeEnum.DATA_ERROR);
		}
		return Result.ok(customerLoginLog);
	}

}
