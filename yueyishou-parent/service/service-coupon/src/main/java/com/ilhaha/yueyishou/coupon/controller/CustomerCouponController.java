package com.ilhaha.yueyishou.coupon.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ilhaha.yueyishou.coupon.service.ICustomerCouponService;
import com.ilhaha.yueyishou.model.entity.coupon.CustomerCoupon;
import com.ilhaha.yueyishou.common.execption.YueYiShouException;
import com.ilhaha.yueyishou.common.result.Result;
import com.ilhaha.yueyishou.common.result.ResultCodeEnum;
import com.ilhaha.yueyishou.model.form.coupon.AvailableCouponForm;
import com.ilhaha.yueyishou.model.form.coupon.FreeIssueForm;
import com.ilhaha.yueyishou.model.vo.coupon.AvailableCouponVo;
import com.ilhaha.yueyishou.model.vo.order.CalculateActualOrderVo;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/customerCoupon")
@Slf4j
public class CustomerCouponController  {
	@Resource
	private ICustomerCouponService customerCouponService;

	/**
	 * 获取顾客在订单中可使用的服务抵扣劵
	 * @param availableCouponForm
	 * @return
	 */
	@PostMapping("/available")
	public Result<List<AvailableCouponVo>> getAvailableCustomerServiceCoupons(@RequestBody AvailableCouponForm availableCouponForm) {
		return Result.ok(customerCouponService.getAvailableCustomerServiceCoupons(availableCouponForm));
	}

	/**
	 *  免费发放服务抵扣劵
	 * @param freeIssueFormList
	 * @return
	 */
	@PostMapping("/free/issue")
	public Result<Boolean> freeIssue(@RequestBody List<FreeIssueForm> freeIssueFormList){
		return Result.ok(customerCouponService.freeIssue(freeIssueFormList));
	}
	
	/**
	 * 分页列表查询
	 *
	 * @param customerCoupon
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/list")
	public Result<IPage<CustomerCoupon>> queryPageList(CustomerCoupon customerCoupon,
													   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
													   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
													   HttpServletRequest req) {
		LambdaQueryWrapper<CustomerCoupon> customerCouponLambdaQueryWrapper = new LambdaQueryWrapper<>();
		Page<CustomerCoupon> page = new Page<CustomerCoupon>(pageNo, pageSize);
		IPage<CustomerCoupon> pageList = customerCouponService.page(page, customerCouponLambdaQueryWrapper);
		return Result.ok(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param customerCoupon
	 * @return
	 */
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody CustomerCoupon customerCoupon) {
		customerCouponService.save(customerCoupon);
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param customerCoupon
	 * @return
	 */
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody CustomerCoupon customerCoupon) {
		customerCouponService.updateById(customerCoupon);
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
		customerCouponService.removeById(id);
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
		this.customerCouponService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@GetMapping(value = "/queryById")
	public Result<CustomerCoupon> queryById(@RequestParam(name="id",required=true) String id) {
		CustomerCoupon customerCoupon = customerCouponService.getById(id);
		if(customerCoupon==null) {
			throw new YueYiShouException(ResultCodeEnum.DATA_ERROR);
		}
		return Result.ok(customerCoupon);
	}

}
