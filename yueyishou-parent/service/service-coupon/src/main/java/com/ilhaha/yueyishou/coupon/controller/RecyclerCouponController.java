package com.ilhaha.yueyishou.coupon.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ilhaha.yueyishou.coupon.service.IRecyclerCouponService;
import com.ilhaha.yueyishou.model.entity.coupon.RecyclerCoupon;
import com.ilhaha.yueyishou.common.execption.YueYiShouException;
import com.ilhaha.yueyishou.common.result.Result;
import com.ilhaha.yueyishou.common.result.ResultCodeEnum;
import com.ilhaha.yueyishou.model.form.coupon.AvailableCouponForm;
import com.ilhaha.yueyishou.model.form.coupon.FreeIssueForm;
import com.ilhaha.yueyishou.model.form.coupon.UseCouponFrom;
import com.ilhaha.yueyishou.model.vo.coupon.AvailableCouponVo;
import com.ilhaha.yueyishou.model.vo.coupon.CouponNotUsedVo;
import com.ilhaha.yueyishou.model.vo.coupon.ExistingCouponVo;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/recyclerCoupon")
@Slf4j
public class RecyclerCouponController  {
	@Resource
	private IRecyclerCouponService recyclerCouponService;

	/**
	 * 获取回收员已有的服务抵扣劵
	 * @param recyclerIds
	 * @return
	 */
	@PostMapping("/existing")
	public Result<List<ExistingCouponVo>> getExistingCoupons(@RequestBody List<Long> recyclerIds){
		return Result.ok(recyclerCouponService.getExistingCoupons(recyclerIds));
	}

	/**
	 * 获取回收员的服务抵扣劵
	 * @param recyclerId
	 * @return
	 */
	@GetMapping("/not/used/{recyclerId}")
	public Result<List<CouponNotUsedVo>> getNotUsedCoupon(@PathVariable("recyclerId") Long recyclerId){
		return Result.ok(recyclerCouponService.getNotUsedCoupon(recyclerId));
	}


	/**
	 * 回收员使用服务抵扣劵
	 * @param useCouponFrom
	 * @return
	 */
	@PostMapping("/use")
	public Result<Boolean> useCoupon(@RequestBody UseCouponFrom useCouponFrom){
		return Result.ok(recyclerCouponService.useCoupon(useCouponFrom));
	}

	/**
	 * 获取回收员在订单中可使用的服务抵扣劵
	 * @param availableCouponForm
	 * @return
	 */
	@PostMapping("/available")
	public Result<List<AvailableCouponVo>> getAvailableCustomerServiceCoupons(@RequestBody AvailableCouponForm availableCouponForm) {
		return Result.ok(recyclerCouponService.getAvailableCustomerServiceCoupons(availableCouponForm));
	}

	/**
	 *  免费发放服务抵扣劵
	 * @param freeIssueFormList
	 * @param recyclerCount
	 * @return
	 */
	@PostMapping("/free/issue/{recyclerCount}")
	public Result<Boolean> freeIssue(@RequestBody List<FreeIssueForm> freeIssueFormList,
									 @PathVariable("recyclerCount") Integer recyclerCount){
		return Result.ok(recyclerCouponService.freeIssue(freeIssueFormList,recyclerCount));
	}


	/**
	 * 分页列表查询
	 *
	 * @param recyclerCoupon
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/list")
	public Result<IPage<RecyclerCoupon>> queryPageList(RecyclerCoupon recyclerCoupon,
													   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
													   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
													   HttpServletRequest req) {
		LambdaQueryWrapper<RecyclerCoupon> recyclerCouponLambdaQueryWrapper = new LambdaQueryWrapper<>();
		Page<RecyclerCoupon> page = new Page<RecyclerCoupon>(pageNo, pageSize);
		IPage<RecyclerCoupon> pageList = recyclerCouponService.page(page, recyclerCouponLambdaQueryWrapper);
		return Result.ok(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param recyclerCoupon
	 * @return
	 */
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody RecyclerCoupon recyclerCoupon) {
		recyclerCouponService.save(recyclerCoupon);
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param recyclerCoupon
	 * @return
	 */
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody RecyclerCoupon recyclerCoupon) {
		recyclerCouponService.updateById(recyclerCoupon);
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
		recyclerCouponService.removeById(id);
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
		this.recyclerCouponService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@GetMapping(value = "/queryById")
	public Result<RecyclerCoupon> queryById(@RequestParam(name="id",required=true) String id) {
		RecyclerCoupon recyclerCoupon = recyclerCouponService.getById(id);
		if(recyclerCoupon==null) {
			throw new YueYiShouException(ResultCodeEnum.DATA_ERROR);
		}
		return Result.ok(recyclerCoupon);
	}

}
