package com.ilhaha.yueyishou.recycler.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ilhaha.yueyishou.common.execption.YueYiShouException;
import com.ilhaha.yueyishou.model.entity.recycler.RecyclerAccount;
import com.ilhaha.yueyishou.model.form.customer.CustomerWithdrawForm;
import com.ilhaha.yueyishou.model.form.order.SettlementForm;
import com.ilhaha.yueyishou.model.form.recycler.RecyclerAccountForm;
import com.ilhaha.yueyishou.model.form.recycler.RecyclerWithdrawForm;
import com.ilhaha.yueyishou.model.vo.recycler.RecyclerAccountVo;
import com.ilhaha.yueyishou.recycler.service.IRecyclerAccountService;
import com.ilhaha.yueyishou.common.result.Result;
import com.ilhaha.yueyishou.common.result.ResultCodeEnum;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@RestController
@RequestMapping("/recyclerAccount")
@Slf4j
public class RecyclerAccountController {
	@Resource
	private IRecyclerAccountService recyclerAccountService;

	/**
	 * 回收员拒收订单得到补偿
	 * @param recyclerWithdrawForm
	 * @return
	 */
	@PostMapping("/reject/compensate")
	public Result<Boolean> rejectCompensate(@RequestBody RecyclerWithdrawForm recyclerWithdrawForm){
		return Result.ok(recyclerAccountService.rejectCompensate(recyclerWithdrawForm));
	}

	/**
	 * 回收员距离预约时间不足60分钟付费取消
	 * @param recyclerWithdrawForm
	 * @return
	 */
	@PostMapping("/chargeCancellation")
	public Result<Boolean> chargeCancellationIfWithinOneHour(@RequestBody RecyclerWithdrawForm recyclerWithdrawForm){
		return Result.ok(recyclerAccountService.chargeCancellationIfWithinOneHour((recyclerWithdrawForm)));
	}

	/**
	 * 顾客取消已超过免费时限，需支付相关费用取消订单
	 * @param recyclerWithdrawForm
	 * @return
	 */
	@PostMapping("/processPaidCancellation")
	public Result<Boolean> processPaidCancellation(@RequestBody RecyclerWithdrawForm recyclerWithdrawForm){
		return Result.ok(recyclerAccountService.processPaidCancellation(recyclerWithdrawForm));
	}

	/**
	 * 超过预约时间未到达，需回收员赔偿取消
	 * @param recyclerWithdrawForm
	 * @return
	 */
	@PostMapping("/cancelOrderIfOverdue")
	public Result<Boolean> cancelOrderIfOverdue(@RequestBody RecyclerWithdrawForm recyclerWithdrawForm){
		return Result.ok(recyclerAccountService.cancelOrderIfOverdue(recyclerWithdrawForm));
	}

	/**
	 * 结算订单
	 * @param recyclerWithdrawForm
	 * @return
	 */
	@PostMapping("/settlement")
	public Result<Boolean> settlement(@RequestBody RecyclerWithdrawForm recyclerWithdrawForm){
		return Result.ok(recyclerAccountService.settlement(recyclerWithdrawForm));
	}

	/**
	 * 回收员提现到微信零钱
	 * @param recyclerWithdrawForm
	 * @return
	 */
	@PostMapping("/onWithdraw")
	public Result<Boolean> onWithdraw(@RequestBody RecyclerWithdrawForm recyclerWithdrawForm){
		return Result.ok(recyclerAccountService.onWithdraw(recyclerWithdrawForm));
	}

	/**
	 * 回收员给账号充值
	 * @param recyclerWithdrawForm
	 * @return
	 */
	@PostMapping("/onRecharge")
	public Result<Boolean> onRecharge(@RequestBody RecyclerWithdrawForm recyclerWithdrawForm){
		return Result.ok(recyclerAccountService.onRecharge(recyclerWithdrawForm));
	}

	/**
	 * 获取回收员账户信息
	 * @param recyclerAccountForm
	 * @return
	 */
	@PostMapping("/info")
	public Result<RecyclerAccountVo> getRecyclerAccountInfo(@RequestBody RecyclerAccountForm recyclerAccountForm){
		return Result.ok(recyclerAccountService.getRecyclerAccountInfo(recyclerAccountForm));
	}
	
	/**
	 * 分页列表查询
	 *
	 * @param recyclerAccount
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/list")
	public Result<IPage<RecyclerAccount>> queryPageList(RecyclerAccount recyclerAccount,
														@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
														@RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
														HttpServletRequest req) {
		LambdaQueryWrapper<RecyclerAccount> recyclerAccountLambdaQueryWrapper = new LambdaQueryWrapper<>();
		Page<RecyclerAccount> page = new Page<RecyclerAccount>(pageNo, pageSize);
		IPage<RecyclerAccount> pageList = recyclerAccountService.page(page, recyclerAccountLambdaQueryWrapper);
		return Result.ok(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param recyclerAccount
	 * @return
	 */
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody RecyclerAccount recyclerAccount) {
		recyclerAccountService.save(recyclerAccount);
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param recyclerAccount
	 * @return
	 */
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody RecyclerAccount recyclerAccount) {
		recyclerAccountService.updateById(recyclerAccount);
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
		recyclerAccountService.removeById(id);
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
		this.recyclerAccountService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@GetMapping(value = "/queryById")
	public Result<RecyclerAccount> queryById(@RequestParam(name="id",required=true) String id) {
		RecyclerAccount recyclerAccount = recyclerAccountService.getById(id);
		if(recyclerAccount==null) {
			throw new YueYiShouException(ResultCodeEnum.DATA_ERROR);
		}
		return Result.ok(recyclerAccount);
	}

}
