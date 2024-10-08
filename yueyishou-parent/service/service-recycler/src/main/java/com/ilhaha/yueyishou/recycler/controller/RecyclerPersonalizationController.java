package com.ilhaha.yueyishou.recycler.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ilhaha.yueyishou.common.execption.YueYiShouException;
import com.ilhaha.yueyishou.model.entity.recycler.RecyclerPersonalization;
import com.ilhaha.yueyishou.model.form.recycler.OrderSettingForm;
import com.ilhaha.yueyishou.model.vo.recycler.OrderSettingVo;
import com.ilhaha.yueyishou.recycler.service.IRecyclerPersonalizationService;
import com.ilhaha.yueyishou.common.result.Result;
import com.ilhaha.yueyishou.common.result.ResultCodeEnum;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
@RestController
@RequestMapping("/recyclerPersonalization")
@Slf4j
public class RecyclerPersonalizationController {
	@Resource
	private IRecyclerPersonalizationService recyclerPersonalizationService;

	/**
	 * 修改回收员接单设置
	 * @param orderSettingForm
	 * @return
	 */
	@PostMapping("/update/order/setting")
	public Result<Boolean> updateOrderSetting(@RequestBody OrderSettingForm orderSettingForm) {
		return Result.ok(recyclerPersonalizationService.updateOrderSetting(orderSettingForm));
	}

	/**
	 * 获取回收员的接单设置
	 * @param recyclerId
	 * @return
	 */
	@GetMapping("/order/setting/{recyclerId}")
	public Result<OrderSettingVo> orderSetting(@PathVariable("recyclerId") Long recyclerId) {
		return Result.ok(recyclerPersonalizationService.orderSetting(recyclerId));
	}


	/**
	 * 修改回收员服务状态
	 * @param recyclerId
	 * @param serviceStatus
	 * @return
	 */
	@PostMapping("/switch/service/{recyclerId}/{serviceStatus}")
	public Result<Boolean> takeOrders(@PathVariable("recyclerId") Long recyclerId,
									  @PathVariable("serviceStatus") Integer serviceStatus){
		return Result.ok(recyclerPersonalizationService.takeOrders(recyclerId,serviceStatus));
	}

	/**
	 * 根据回收员ID获取回收员的个性化设置
	 * @param recyclerId
	 * @return
	 */
	@GetMapping("/info/by/{recyclerId}")
	public Result<RecyclerPersonalization> getPersonalizationByRecyclerId(@PathVariable("recyclerId") Long recyclerId){
		return Result.ok(recyclerPersonalizationService.getPersonalizationByRecyclerId(recyclerId));
	}
	
	/**
	 * 分页列表查询
	 *
	 * @param recyclerPersonalization
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/list")
	public Result<IPage<RecyclerPersonalization>> queryPageList(RecyclerPersonalization recyclerPersonalization,
																@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
																@RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
																HttpServletRequest req) {
		LambdaQueryWrapper<RecyclerPersonalization> recyclerPersonalizationLambdaQueryWrapper = new LambdaQueryWrapper<>();
		Page<RecyclerPersonalization> page = new Page<RecyclerPersonalization>(pageNo, pageSize);
		IPage<RecyclerPersonalization> pageList = recyclerPersonalizationService.page(page, recyclerPersonalizationLambdaQueryWrapper);
		return Result.ok(pageList);
	}
	
	/**
	 *   添加回收员个性化设置
	 *
	 * @param recyclerPersonalization
	 * @return
	 */
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody RecyclerPersonalization recyclerPersonalization) {
		recyclerPersonalizationService.save(recyclerPersonalization);
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param recyclerPersonalization
	 * @return
	 */
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody RecyclerPersonalization recyclerPersonalization) {
		recyclerPersonalizationService.updateById(recyclerPersonalization);
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
		recyclerPersonalizationService.removeById(id);
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
		this.recyclerPersonalizationService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@GetMapping(value = "/queryById")
	public Result<RecyclerPersonalization> queryById(@RequestParam(name="id",required=true) String id) {
		RecyclerPersonalization recyclerPersonalization = recyclerPersonalizationService.getById(id);
		if(recyclerPersonalization==null) {
			throw new YueYiShouException(ResultCodeEnum.DATA_ERROR);
		}
		return Result.ok(recyclerPersonalization);
	}

}
