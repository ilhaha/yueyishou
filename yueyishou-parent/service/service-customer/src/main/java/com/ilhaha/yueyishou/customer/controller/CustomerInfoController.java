package com.ilhaha.yueyishou.customer.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ilhaha.yueyishou.model.entity.customer.CustomerInfo;
import com.ilhaha.yueyishou.customer.service.ICustomerInfoService;
import com.ilhaha.yueyishou.common.execption.YueYiShouException;
import com.ilhaha.yueyishou.model.form.customer.UpdateCustomerBaseInfoForm;
import com.ilhaha.yueyishou.model.form.customer.UpdateCustomerStatusForm;
import com.ilhaha.yueyishou.common.result.Result;
import com.ilhaha.yueyishou.common.result.ResultCodeEnum;
import com.ilhaha.yueyishou.model.form.recycler.RecyclerApplyForm;
import com.ilhaha.yueyishou.model.vo.customer.CustomerLoginInfoVo;
import com.ilhaha.yueyishou.model.vo.customer.CustomerMyVo;
import com.ilhaha.yueyishou.model.vo.customer.ExamineInfoVo;
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
	 * 获取顾客认证回收员的审核反馈信息
	 * @param customerId
	 * @return
	 */
	@GetMapping("/audit/feedback/{customerId}")
	public Result<ExamineInfoVo> auditFeedback(@PathVariable("customerId") Long customerId){
		return Result.ok(customerInfoService.auditFeedback(customerId));
	}

	/**
	 * 获取顾客我的页面初始信息
	 * @param customerId
	 * @return
	 */
	@GetMapping("/my/{customerId}")
	public Result<CustomerMyVo> getMy(@PathVariable Long customerId){
		return Result.ok(customerInfoService.getMy(customerId));
	}


	/**
	 * 更新顾客基本信息
	 * @param updateCustomerBaseInfoForm
	 * @return
	 */
	@PostMapping("/update/base/info")
	public Result<Boolean> updateBaseInfo(@RequestBody UpdateCustomerBaseInfoForm updateCustomerBaseInfoForm){
		return Result.ok(customerInfoService.updateBaseInfo(updateCustomerBaseInfoForm));
	}

	/**
	 * 认证成为回收员
	 * @param recyclerApplyForm
	 * @return
	 */
	@PostMapping("/auth/recycler")
	public Result<Boolean> authRecycler(@RequestBody RecyclerApplyForm recyclerApplyForm){
		return Result.ok(customerInfoService.authRecycler(recyclerApplyForm));
	}


	/**
	 * 获取顾客登录之后的顾客信息
	 * @param customerId
	 * @return
	 */
	@GetMapping("/login/info/{customerId}")
	public Result<CustomerLoginInfoVo> getLoginInfo(@PathVariable("customerId") Long customerId){
		return Result.ok(customerInfoService.getLoginInfo(customerId));
	}

	/**
	 * 修改客户状态
	 * @param updateCustomerStatusForm
	 * @return
	 */
	@PostMapping("/switch/status")
	public Result<String> switchStatus(@RequestBody UpdateCustomerStatusForm updateCustomerStatusForm){
		return Result.ok(customerInfoService.switchStatus(updateCustomerStatusForm));
	}

	/**
	 * 小程序授权登录
	 * @param code
	 * @return
	 */
	@GetMapping("/login/{code}")
	public Result<String> login(@PathVariable("code") String code){
		return Result.ok(customerInfoService.login(code));
	}


	/**
	 * 客户分页列表查询
	 *
	 * @param customerInfo
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	@PostMapping("/list")
	public Result<Page<CustomerInfo>> queryPageList(@RequestBody CustomerInfo customerInfo,
													 @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
													 @RequestParam(name="pageSize", defaultValue="10") Integer pageSize) {


		Page<CustomerInfo> page = new Page<CustomerInfo>(pageNo, pageSize);
		Page<CustomerInfo> pageList = customerInfoService.queryPageList(page,customerInfo);
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
	public Result<CustomerInfo> queryById(@RequestParam(name="id",required=true) Long id) {
		CustomerInfo customerInfo = customerInfoService.getById(id);
		if(customerInfo==null) {
			throw new YueYiShouException(ResultCodeEnum.DATA_ERROR);
		}
		return Result.ok(customerInfo);
	}

}
