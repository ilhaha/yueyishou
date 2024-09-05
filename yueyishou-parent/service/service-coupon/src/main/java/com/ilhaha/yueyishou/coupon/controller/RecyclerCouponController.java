package com.ilhaha.yueyishou.coupon.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ilhaha.yueyishou.coupon.service.IRecyclerCouponService;
import com.ilhaha.yueyishou.entity.coupon.RecyclerCoupon;
import com.ilhaha.yueyishou.execption.YueYiShouException;
import com.ilhaha.yueyishou.result.Result;
import com.ilhaha.yueyishou.result.ResultCodeEnum;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@RestController
@RequestMapping("/recyclerCoupon")
@Slf4j
public class RecyclerCouponController  {
	@Resource
	private IRecyclerCouponService recyclerCouponService;
	
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
