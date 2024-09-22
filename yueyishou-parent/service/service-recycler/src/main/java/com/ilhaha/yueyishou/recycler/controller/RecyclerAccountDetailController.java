package com.ilhaha.yueyishou.recycler.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ilhaha.yueyishou.common.execption.YueYiShouException;
import com.ilhaha.yueyishou.model.entity.recycler.RecyclerAccountDetail;
import com.ilhaha.yueyishou.recycler.service.IRecyclerAccountDetailService;
import com.ilhaha.yueyishou.common.result.Result;
import com.ilhaha.yueyishou.common.result.ResultCodeEnum;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@RestController
@RequestMapping("/recyclerAccountDetail")
@Slf4j
public class RecyclerAccountDetailController {
	@Resource
	private IRecyclerAccountDetailService recyclerAccountDetailService;
	
	/**
	 * 分页列表查询
	 *
	 * @param recyclerAccountDetail
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/list")
	public Result<IPage<RecyclerAccountDetail>> queryPageList(RecyclerAccountDetail recyclerAccountDetail,
															  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
															  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
															  HttpServletRequest req) {
		LambdaQueryWrapper<RecyclerAccountDetail> recyclerAccountDetailLambdaQueryWrapper = new LambdaQueryWrapper<>();
		Page<RecyclerAccountDetail> page = new Page<RecyclerAccountDetail>(pageNo, pageSize);
		IPage<RecyclerAccountDetail> pageList = recyclerAccountDetailService.page(page, recyclerAccountDetailLambdaQueryWrapper);
		return Result.ok(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param recyclerAccountDetail
	 * @return
	 */
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody RecyclerAccountDetail recyclerAccountDetail) {
		recyclerAccountDetailService.save(recyclerAccountDetail);
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param recyclerAccountDetail
	 * @return
	 */
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody RecyclerAccountDetail recyclerAccountDetail) {
		recyclerAccountDetailService.updateById(recyclerAccountDetail);
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
		recyclerAccountDetailService.removeById(id);
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
		this.recyclerAccountDetailService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	public Result<RecyclerAccountDetail> queryById(@RequestParam(name="id",required=true) String id) {
		RecyclerAccountDetail recyclerAccountDetail = recyclerAccountDetailService.getById(id);
		if(recyclerAccountDetail==null) {
			throw new YueYiShouException(ResultCodeEnum.DATA_ERROR);
		}
		return Result.ok(recyclerAccountDetail);
	}

}
