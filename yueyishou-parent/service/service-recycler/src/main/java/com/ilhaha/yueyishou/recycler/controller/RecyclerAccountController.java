package com.ilhaha.yueyishou.recycler.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ilhaha.yueyishou.execption.YueYiShouException;
import com.ilhaha.yueyishou.entity.recycler.RecyclerAccount;
import com.ilhaha.yueyishou.recycler.service.IRecyclerAccountService;
import com.ilhaha.yueyishou.result.Result;
import com.ilhaha.yueyishou.result.ResultCodeEnum;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@RestController
@RequestMapping("/recyclerAccount")
@Slf4j
public class RecyclerAccountController {
	@Resource
	private IRecyclerAccountService recyclerAccountService;
	
	/**
	 * 分页列表查询
	 *
	 * @param recyclerAccount
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/list")
	public Result<IPage<RecyclerAccount>> queryPageList(RecyclerAccount recyclerAccount,
														@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
														@RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
														HttpServletRequest req) {
		LambdaQueryWrapper<RecyclerAccount> recyclerAccountLambdaQueryWrapper = new LambdaQueryWrapper<>();
		Page<RecyclerAccount> page = new Page<RecyclerAccount>(pageNo, pageSize);
		IPage<RecyclerAccount> pageList = recyclerAccountService.page(page, recyclerAccountLambdaQueryWrapper);
		return Result.ok(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param recyclerAccount
	 * @return
	 */
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody RecyclerAccount recyclerAccount) {
		recyclerAccountService.save(recyclerAccount);
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param recyclerAccount
	 * @return
	 */
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody RecyclerAccount recyclerAccount) {
		recyclerAccountService.updateById(recyclerAccount);
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
		recyclerAccountService.removeById(id);
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
		this.recyclerAccountService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@GetMapping(value = "/queryById")
	public Result<RecyclerAccount> queryById(@RequestParam(name="id",required=true) String id) {
		RecyclerAccount recyclerAccount = recyclerAccountService.getById(id);
		if(recyclerAccount==null) {
			throw new YueYiShouException(ResultCodeEnum.DATA_ERROR);
		}
		return Result.ok(recyclerAccount);
	}

}