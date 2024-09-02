package com.ilhaha.yueyishou.category.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ilhaha.yueyishou.entity.category.CategoryInfo;
import com.ilhaha.yueyishou.category.service.ICategoryInfoService;
import com.ilhaha.yueyishou.execption.YueYiShouException;
import com.ilhaha.yueyishou.result.Result;
import com.ilhaha.yueyishou.result.ResultCodeEnum;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@RestController
@RequestMapping("/categoryInfo")
@Slf4j
public class CategoryInfoController {
	@Resource
	private ICategoryInfoService categoryInfoService;
	
	/**
	 * 分页列表查询
	 *
	 * @param categoryInfo
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/list")
	public Result<IPage<CategoryInfo>> queryPageList(CategoryInfo categoryInfo,
													 @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
													 @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
													 HttpServletRequest req) {
		LambdaQueryWrapper<CategoryInfo> categoryInfoLambdaQueryWrapper = new LambdaQueryWrapper<>();
		Page<CategoryInfo> page = new Page<CategoryInfo>(pageNo, pageSize);
		IPage<CategoryInfo> pageList = categoryInfoService.page(page, categoryInfoLambdaQueryWrapper);
		return Result.ok(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param categoryInfo
	 * @return
	 */
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody CategoryInfo categoryInfo) {
		categoryInfoService.save(categoryInfo);
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param categoryInfo
	 * @return
	 */
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody CategoryInfo categoryInfo) {
		categoryInfoService.updateById(categoryInfo);
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
		categoryInfoService.removeById(id);
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
		this.categoryInfoService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@GetMapping(value = "/queryById")
	public Result<CategoryInfo> queryById(@RequestParam(name="id",required=true) String id) {
		CategoryInfo categoryInfo = categoryInfoService.getById(id);
		if(categoryInfo==null) {
			throw new YueYiShouException(ResultCodeEnum.DATA_ERROR);
		}
		return Result.ok(categoryInfo);
	}

}
