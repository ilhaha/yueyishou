package com.ilhaha.yueyishou.customer.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ilhaha.yueyishou.entity.customer.CustomerAccountDetail;
import com.ilhaha.yueyishou.customer.service.ICustomerAccountDetailService;
import com.ilhaha.yueyishou.execption.YueYiShouException;
import com.ilhaha.yueyishou.result.Result;
import com.ilhaha.yueyishou.result.ResultCodeEnum;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@RestController
@RequestMapping("/customerAccountDetail")
@Slf4j
public class CustomerAccountDetailController {
	@Resource
	private ICustomerAccountDetailService customerAccountDetailService;
	
	/**
	 * 分页列表查询
	 *
	 * @param customerAccountDetail
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/list")
	public Result<IPage<CustomerAccountDetail>> queryPageList(CustomerAccountDetail customerAccountDetail,
															  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
															  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
															  HttpServletRequest req) {
		LambdaQueryWrapper<CustomerAccountDetail> customerAccountDetailLambdaQueryWrapper = new LambdaQueryWrapper<CustomerAccountDetail>();
		Page<CustomerAccountDetail> page = new Page<CustomerAccountDetail>(pageNo, pageSize);
		IPage<CustomerAccountDetail> pageList = customerAccountDetailService.page(page, customerAccountDetailLambdaQueryWrapper);
		return Result.ok(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param customerAccountDetail
	 * @return
	 */
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody CustomerAccountDetail customerAccountDetail) {
		customerAccountDetailService.save(customerAccountDetail);
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param customerAccountDetail
	 * @return
	 */
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody CustomerAccountDetail customerAccountDetail) {
		customerAccountDetailService.updateById(customerAccountDetail);
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
		customerAccountDetailService.removeById(id);
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
		this.customerAccountDetailService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@GetMapping(value = "/queryById")
	public Result<CustomerAccountDetail> queryById(@RequestParam(name="id",required=true) String id) {
		CustomerAccountDetail customerAccountDetail = customerAccountDetailService.getById(id);
		if(customerAccountDetail==null) {
			throw new YueYiShouException(ResultCodeEnum.DATA_ERROR);
		}
		return Result.ok(customerAccountDetail);
	}

}
