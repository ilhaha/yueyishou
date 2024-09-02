package com.ilhaha.yueyishou.order.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ilhaha.yueyishou.execption.YueYiShouException;
import com.ilhaha.yueyishou.entity.order.OrderInfo;
import com.ilhaha.yueyishou.order.service.IOrderInfoService;
import com.ilhaha.yueyishou.result.Result;
import com.ilhaha.yueyishou.result.ResultCodeEnum;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@RestController
@RequestMapping("/orderInfo")
@Slf4j
public class OrderInfoController {
	@Resource
	private IOrderInfoService orderInfoService;
	
	/**
	 * 分页列表查询
	 *
	 * @param orderInfo
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/list")
	public Result<IPage<OrderInfo>> queryPageList(OrderInfo orderInfo,
												  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
												  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
												  HttpServletRequest req) {
		LambdaQueryWrapper<OrderInfo> orderInfoLambdaQueryWrapper = new LambdaQueryWrapper<>();
		Page<OrderInfo> page = new Page<OrderInfo>(pageNo, pageSize);
		IPage<OrderInfo> pageList = orderInfoService.page(page, orderInfoLambdaQueryWrapper);
		return Result.ok(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param orderInfo
	 * @return
	 */
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody OrderInfo orderInfo) {
		orderInfoService.save(orderInfo);
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param orderInfo
	 * @return
	 */
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody OrderInfo orderInfo) {
		orderInfoService.updateById(orderInfo);
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
		orderInfoService.removeById(id);
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
		this.orderInfoService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@GetMapping(value = "/queryById")
	public Result<OrderInfo> queryById(@RequestParam(name="id",required=true) String id) {
		OrderInfo orderInfo = orderInfoService.getById(id);
		if(orderInfo==null) {
			throw new YueYiShouException(ResultCodeEnum.DATA_ERROR);
		}
		return Result.ok(orderInfo);
	}
}
