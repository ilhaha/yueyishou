package com.ilhaha.yueyishou.customer.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ilhaha.yueyishou.model.entity.customer.CustomerAccount;
import com.ilhaha.yueyishou.customer.service.ICustomerAccountService;
import com.ilhaha.yueyishou.common.execption.YueYiShouException;
import com.ilhaha.yueyishou.common.result.Result;
import com.ilhaha.yueyishou.common.result.ResultCodeEnum;
import com.ilhaha.yueyishou.model.form.customer.CustomerAccountForm;
import com.ilhaha.yueyishou.model.form.customer.CustomerWithdrawForm;
import com.ilhaha.yueyishou.model.form.recycler.RecyclerAccountForm;
import com.ilhaha.yueyishou.model.form.recycler.RecyclerWithdrawForm;
import com.ilhaha.yueyishou.model.vo.customer.CustomerAccountVo;
import com.ilhaha.yueyishou.model.vo.recycler.RecyclerAccountVo;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@RestController
@RequestMapping("/customerAccount")
@Slf4j
public class CustomerAccountController  {

	@Resource
	private ICustomerAccountService customerAccountService;

	/**
	 * 顾客提现到微信零钱
	 * @param customerWithdrawForm
	 * @return
	 */
	@PostMapping("/onWithdraw")
	public Result<Boolean> onWithdraw(@RequestBody CustomerWithdrawForm customerWithdrawForm){
		return Result.ok(customerAccountService.onWithdraw(customerWithdrawForm));
	}

	/**
	 * 获取顾客账户信息
	 * @param customerAccountForm
	 * @return
	 */
	@PostMapping("/info")
	public Result<CustomerAccountVo> getCustomerAccountInfo(@RequestBody CustomerAccountForm customerAccountForm){
		return Result.ok(customerAccountService.getCustomerAccountInfo(customerAccountForm));
	}

	/**
	 * 结算顾客订单
	 * @param customerWithdrawForm
	 * @return
	 */
	@PostMapping("/settlement")
	public Result<Boolean> settlement(@RequestBody CustomerWithdrawForm customerWithdrawForm){
		return Result.ok(customerAccountService.settlement(customerWithdrawForm));
	}
	
	/**
	 * 分页列表查询
	 *
	 * @param customerAccount
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/list")
	public Result<IPage<CustomerAccount>> queryPageList(CustomerAccount customerAccount,
														@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
														@RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
														HttpServletRequest req) {
		LambdaQueryWrapper<CustomerAccount> customerAccountLambdaQueryWrapper = new LambdaQueryWrapper<>();
		Page<CustomerAccount> page = new Page<CustomerAccount>(pageNo, pageSize);
		IPage<CustomerAccount> pageList = customerAccountService.page(page, customerAccountLambdaQueryWrapper);
		return Result.ok(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param customerAccount
	 * @return
	 */
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody CustomerAccount customerAccount) {
		customerAccountService.save(customerAccount);
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param customerAccount
	 * @return
	 */
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody CustomerAccount customerAccount) {
		customerAccountService.updateById(customerAccount);
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
		customerAccountService.removeById(id);
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
		this.customerAccountService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@GetMapping(value = "/queryById")
	public Result<CustomerAccount> queryById(@RequestParam(name="id",required=true) String id) {
		CustomerAccount customerAccount = customerAccountService.getById(id);
		if(customerAccount==null) {
			throw new YueYiShouException(ResultCodeEnum.DATA_ERROR);
		}
		return Result.ok(customerAccount);
	}

}
