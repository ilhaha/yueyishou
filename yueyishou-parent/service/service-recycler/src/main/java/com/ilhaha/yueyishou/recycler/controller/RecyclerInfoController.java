package com.ilhaha.yueyishou.recycler.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ilhaha.yueyishou.entity.recycler.RecyclerInfo;
import com.ilhaha.yueyishou.execption.YueYiShouException;
import com.ilhaha.yueyishou.form.recycler.RecyclerAuthForm;
import com.ilhaha.yueyishou.form.recycler.UpdateRecyclerStatusForm;
import com.ilhaha.yueyishou.recycler.service.IRecyclerInfoService;
import com.ilhaha.yueyishou.result.Result;
import com.ilhaha.yueyishou.result.ResultCodeEnum;
import com.ilhaha.yueyishou.vo.recycler.CosUploadVo;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;

@RestController
@RequestMapping("/recyclerInfo")
@Slf4j
public class RecyclerInfoController {
	@Resource
	private IRecyclerInfoService recyclerInfoService;

	/**
	 * 回收员上传图片
	 * @param file
	 * @return
	 */
	@PostMapping("/upload")
	public Result<CosUploadVo> upload(@RequestPart("file") MultipartFile file, @RequestParam(name = "path") String path){
		return recyclerInfoService.upload(file,path);
	}

	/**
	 * 回收员审核
	 * @param recyclerAuthForm
	 * @return
	 */
	@PostMapping("/auth")
	public Result<String> auth(@RequestBody RecyclerAuthForm recyclerAuthForm){
		return Result.ok(recyclerInfoService.auth(recyclerAuthForm));
	}

	/**
	 * 回收员状态切换
	 * @param updateRecyclerStatusForm
	 * @return
	 */
	@PostMapping("/switch/status")
	public Result<String> switchStatus(@RequestBody UpdateRecyclerStatusForm updateRecyclerStatusForm){
		return Result.ok(recyclerInfoService.switchStatus(updateRecyclerStatusForm));
	}
	
	/**
	 * 回收员分页列表查询
	 *
	 * @param recyclerInfo
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	@PostMapping(value = "/list")
	public Result<Page<RecyclerInfo>> queryPageList(@RequestBody RecyclerInfo recyclerInfo,
													 @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
													 @RequestParam(name="pageSize", defaultValue="10") Integer pageSize) {
		return Result.ok(recyclerInfoService.queryPageList(recyclerInfo,pageNo,pageSize));
	}
	
	/**
	 *   添加
	 *
	 * @param recyclerInfo
	 * @return
	 */
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody RecyclerInfo recyclerInfo) {
		recyclerInfoService.save(recyclerInfo);
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param recyclerInfo
	 * @return
	 */
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody RecyclerInfo recyclerInfo) {
		recyclerInfoService.updateById(recyclerInfo);
		return Result.ok("编辑成功!");
	}
	
	/**
	 *   通过id删除回收员
	 *
	 * @param id
	 * @return
	 */
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		recyclerInfoService.removeById(id);
		return Result.ok("删除成功!");
	}
	
	/**
	 *  批量删除回收员
	 *
	 * @param ids
	 * @return
	 */
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.recyclerInfoService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@GetMapping(value = "/queryById")
	public Result<RecyclerInfo> queryById(@RequestParam(name="id",required=true) String id) {
		RecyclerInfo recyclerInfo = recyclerInfoService.getById(id);
		if(recyclerInfo==null) {
			throw new YueYiShouException(ResultCodeEnum.DATA_ERROR);
		}
		return Result.ok(recyclerInfo);
	}
}
