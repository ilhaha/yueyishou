package com.ilhaha.yueyishou.recycler.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ilhaha.yueyishou.execption.YueYiShouException;
import com.ilhaha.yueyishou.entity.recycler.RecyclerLoginLog;
import com.ilhaha.yueyishou.recycler.service.IRecyclerLoginLogService;
import com.ilhaha.yueyishou.result.Result;
import com.ilhaha.yueyishou.result.ResultCodeEnum;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@RestController
@RequestMapping("/recyclerLoginLog")
@Slf4j
public class RecyclerLoginLogController {
	@Resource
	private IRecyclerLoginLogService recyclerLoginLogService;
	
	/**
	 * 分页列表查询
	 *
	 * @param recyclerLoginLog
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/list")
	public Result<IPage<RecyclerLoginLog>> queryPageList(RecyclerLoginLog recyclerLoginLog,
														 @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
														 @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
														 HttpServletRequest req) {
		LambdaQueryWrapper<RecyclerLoginLog> recyclerLoginLogLambdaQueryWrapper = new LambdaQueryWrapper<>();
		Page<RecyclerLoginLog> page = new Page<RecyclerLoginLog>(pageNo, pageSize);
		IPage<RecyclerLoginLog> pageList = recyclerLoginLogService.page(page, recyclerLoginLogLambdaQueryWrapper);
		return Result.ok(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param recyclerLoginLog
	 * @return
	 */
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody RecyclerLoginLog recyclerLoginLog) {
		recyclerLoginLogService.save(recyclerLoginLog);
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param recyclerLoginLog
	 * @return
	 */
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody RecyclerLoginLog recyclerLoginLog) {
		recyclerLoginLogService.updateById(recyclerLoginLog);
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
		recyclerLoginLogService.removeById(id);
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
		this.recyclerLoginLogService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@GetMapping(value = "/queryById")
	public Result<RecyclerLoginLog> queryById(@RequestParam(name="id",required=true) String id) {
		RecyclerLoginLog recyclerLoginLog = recyclerLoginLogService.getById(id);
		if(recyclerLoginLog==null) {
			throw new YueYiShouException(ResultCodeEnum.DATA_ERROR);
		}
		return Result.ok(recyclerLoginLog);
	}
}
