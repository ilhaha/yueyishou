package org.jeecg.modules.mgr.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ilhaha.yueyishou.model.entity.coupon.CouponInfo;

import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.mgr.service.CouponService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @Author ilhaha
 * @Create 2024/9/6 08:18
 * @Version 1.0
 */
@RestController
@RequestMapping("/coupon")
public class CouponController {

    @Resource
    private CouponService couponService;

    /**
     * 服务抵扣劵分页查询
     * @param couponInfo
     * @param pageNo
     * @param pageSize
     * @return
     */
    @GetMapping(value = "/list")
    public Result<Page<CouponInfo>> queryPageList(CouponInfo couponInfo,
                                                  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize){
        return Result.OK(couponService.queryPageList(couponInfo,pageNo,pageSize));
    }

    /**
     *   添加
     *
     * @param couponInfo
     * @return
     */
    @PostMapping(value = "/add")
    Result<String> add(@RequestBody CouponInfo couponInfo){
        return Result.OK(couponService.add(couponInfo));
    };

    /**
     *  编辑
     *
     * @param couponInfo
     * @return
     */
    @PutMapping(value = "/edit")
    Result<String> edit(@RequestBody CouponInfo couponInfo){
        return Result.OK(couponService.edit(couponInfo));
    };

    /**
     *   通过id删除
     *
     * @param id
     * @return
     */
    @DeleteMapping(value = "/delete")
    Result<String> delete(@RequestParam(name="id",required=true) String id){
        return Result.OK(couponService.delete(id));
    };

    /**
     *  批量删除
     *
     * @param ids
     * @return
     */
    @DeleteMapping(value = "/deleteBatch")
    Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
        return Result.OK(couponService.deleteBatch(ids));
    };

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/queryById")
    Result<CouponInfo> queryById(@RequestParam(name="id",required=true) String id) {
        return Result.OK(couponService.queryById(id));
    };
}
