package com.ilhaha.yueyishou.order.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ilhaha.yueyishou.common.anno.LoginVerification;
import com.ilhaha.yueyishou.common.execption.YueYiShouException;
import com.ilhaha.yueyishou.model.entity.order.OrderComment;
import com.ilhaha.yueyishou.model.form.order.ReviewForm;
import com.ilhaha.yueyishou.order.service.IOrderCommentService;
import com.ilhaha.yueyishou.common.result.Result;
import com.ilhaha.yueyishou.common.result.ResultCodeEnum;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@RestController
@RequestMapping("/orderComment")
@Slf4j
public class OrderCommentController {
	@Resource
	private IOrderCommentService orderCommentService;

	/**
	 * 顾客评论
	 * @return
	 */
	@PostMapping("/review")
	public Result<Boolean> review(@RequestBody ReviewForm reviewForm){
		return Result.ok(orderCommentService.review(reviewForm));
	}
	
	/**
	 * 分页列表查询
	 *
	 * @param orderComment
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/list")
	public Result<IPage<OrderComment>> queryPageList(OrderComment orderComment,
													 @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
													 @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
													 HttpServletRequest req) {
		LambdaQueryWrapper<OrderComment> orderCommentLambdaQueryWrapper = new LambdaQueryWrapper<>();
		Page<OrderComment> page = new Page<OrderComment>(pageNo, pageSize);
		IPage<OrderComment> pageList = orderCommentService.page(page, orderCommentLambdaQueryWrapper);
		return Result.ok(pageList);
	}

	/**
	 *   添加
	 *
	 * @param orderComment
	 * @return
	 */
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody OrderComment orderComment) {
		orderCommentService.save(orderComment);
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param orderComment
	 * @return
	 */
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody OrderComment orderComment) {
		orderCommentService.updateById(orderComment);
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
		orderCommentService.removeById(id);
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
		this.orderCommentService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "order_comment-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<OrderComment> queryById(@RequestParam(name="id",required=true) String id) {
		OrderComment orderComment = orderCommentService.getById(id);
		if(orderComment==null) {
			throw new YueYiShouException(ResultCodeEnum.DATA_ERROR);
		}
		return Result.ok(orderComment);
	}
}
