package com.ilhaha.yueyishou.recycler.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ilhaha.yueyishou.common.execption.YueYiShouException;
import com.ilhaha.yueyishou.model.entity.recycler.RecyclerFaceRecognition;
import com.ilhaha.yueyishou.model.entity.recycler.RecyclerPersonalization;
import com.ilhaha.yueyishou.recycler.service.IRecyclerFaceRecognitionService;
import com.ilhaha.yueyishou.common.result.Result;
import com.ilhaha.yueyishou.common.result.ResultCodeEnum;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@RestController
@RequestMapping("/recyclerFaceRecognition")
@Slf4j
public class RecyclerFaceRecognitionController {
	@Resource
	private IRecyclerFaceRecognitionService recyclerFaceRecognitionService;


	/**
	 *   添加
	 *
	 * @param recyclerFaceRecognition
	 * @return
	 */
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody RecyclerFaceRecognition recyclerFaceRecognition) {
		recyclerFaceRecognitionService.save(recyclerFaceRecognition);
		return Result.ok("添加成功！");
	}

	/**
	 *  编辑
	 *
	 * @param recyclerFaceRecognition
	 * @return
	 */
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody RecyclerFaceRecognition recyclerFaceRecognition) {
		recyclerFaceRecognitionService.updateById(recyclerFaceRecognition);
		return Result.ok("编辑成功!");
	}

	/**
	 * 分页列表查询
	 *
	 * @param recyclerFaceRecognition
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/list")
	public Result<IPage<RecyclerFaceRecognition>> queryPageList(RecyclerFaceRecognition recyclerFaceRecognition,
																@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
																@RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
																HttpServletRequest req) {
		LambdaQueryWrapper<RecyclerFaceRecognition> recyclerFaceRecognitionLambdaQueryWrapper = new LambdaQueryWrapper<>();
		Page<RecyclerFaceRecognition> page = new Page<RecyclerFaceRecognition>(pageNo, pageSize);
		IPage<RecyclerFaceRecognition> pageList = recyclerFaceRecognitionService.page(page, recyclerFaceRecognitionLambdaQueryWrapper);
		return Result.ok(pageList);
	}
	

	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		recyclerFaceRecognitionService.removeById(id);
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
		this.recyclerFaceRecognitionService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@GetMapping(value = "/queryById")
	public Result<RecyclerFaceRecognition> queryById(@RequestParam(name="id",required=true) String id) {
		RecyclerFaceRecognition recyclerFaceRecognition = recyclerFaceRecognitionService.getById(id);
		if(recyclerFaceRecognition==null) {
			throw new YueYiShouException(ResultCodeEnum.DATA_ERROR);
		}
		return Result.ok(recyclerFaceRecognition);
	}
}
