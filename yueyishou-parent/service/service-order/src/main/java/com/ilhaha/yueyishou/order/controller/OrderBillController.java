package com.ilhaha.yueyishou.order.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ilhaha.yueyishou.common.execption.YueYiShouException;
import com.ilhaha.yueyishou.model.entity.order.OrderBill;
import com.ilhaha.yueyishou.model.form.order.GenerateBillForm;
import com.ilhaha.yueyishou.order.service.IOrderBillService;
import com.ilhaha.yueyishou.common.result.Result;
import com.ilhaha.yueyishou.common.result.ResultCodeEnum;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@RestController
@RequestMapping("/orderBill")
@Slf4j
public class OrderBillController {
	@Resource
	private IOrderBillService orderBillService;

	/**
	 * 生成订单账单
	 * @param generateBillForm
	 * @return
	 */
	@PostMapping("/generate")
	public Result<Boolean> generateOrder(@RequestBody GenerateBillForm generateBillForm){
		return Result.ok(orderBillService.generateOrder(generateBillForm));
	}
	
	/**
	 * 分页列表查询
	 *
	 * @param orderBill
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/list")
	public Result<IPage<OrderBill>> queryPageList(OrderBill orderBill,
												  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
												  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
												  HttpServletRequest req) {
		LambdaQueryWrapper<OrderBill> orderBillLambdaQueryWrapper = new LambdaQueryWrapper<>();
		Page<OrderBill> page = new Page<OrderBill>(pageNo, pageSize);
		IPage<OrderBill> pageList = orderBillService.page(page, orderBillLambdaQueryWrapper);
		return Result.ok(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param orderBill
	 * @return
	 */
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody OrderBill orderBill) {
		orderBillService.save(orderBill);
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param orderBill
	 * @return
	 */
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody OrderBill orderBill) {
		orderBillService.updateById(orderBill);
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
		orderBillService.removeById(id);
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
		this.orderBillService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@GetMapping(value = "/queryById")
	public Result<OrderBill> queryById(@RequestParam(name="id",required=true) String id) {
		OrderBill orderBill = orderBillService.getById(id);
		if(orderBill==null) {
			throw new YueYiShouException(ResultCodeEnum.DATA_ERROR);
		}
		return Result.ok(orderBill);
	}

}
