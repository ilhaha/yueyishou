package com.ilhaha.yueyishou.coupon.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ilhaha.yueyishou.coupon.service.ICouponInfoService;
import com.ilhaha.yueyishou.model.entity.coupon.CouponInfo;
import com.ilhaha.yueyishou.common.result.Result;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;


@RestController
@RequestMapping("/couponInfo")
@Slf4j
public class CouponInfoController {
	@Resource
	private ICouponInfoService couponInfoService;

	/**
	 * 通过id集合获取服务抵扣劵集合
	 * @param couponIds
	 * @return
	 */
	@PostMapping("/list/byIds")
	public Result<List<CouponInfo>> getListByIds(@RequestBody List<Long> couponIds){
		return Result.ok(couponInfoService.getListByIds(couponIds));
	}
	
	/**
	 * 服务抵扣劵分页查询
	 *
	 * @param couponInfo
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	@PostMapping(value = "/list")
	public Result<Page<CouponInfo>> queryPageList(@RequestBody CouponInfo couponInfo,
												   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
												   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize) {
		LambdaQueryWrapper<CouponInfo> couponInfoLambdaQueryWrapper = new LambdaQueryWrapper<>();
		couponInfoLambdaQueryWrapper.like(StringUtils.hasText(couponInfo.getName()),CouponInfo::getName,couponInfo.getName());
		Page<CouponInfo> page = new Page<CouponInfo>(pageNo, pageSize);
		Page<CouponInfo> pageList = couponInfoService.page(page, couponInfoLambdaQueryWrapper);

		return Result.ok(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param couponInfo
	 * @return
	 */
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody CouponInfo couponInfo) {
		couponInfoService.save(couponInfo);
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param couponInfo
	 * @return
	 */
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody CouponInfo couponInfo) {
		couponInfoService.updateById(couponInfo);
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
		couponInfoService.removeById(id);
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
		this.couponInfoService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@GetMapping(value = "/queryById")
	public Result<CouponInfo> queryById(@RequestParam(name="id",required=true) String id) {
		CouponInfo couponInfo = couponInfoService.getById(id);
		return Result.ok(couponInfo);
	}

}
