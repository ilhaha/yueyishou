package com.ilhaha.yueyishou.coupon.client;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ilhaha.yueyishou.model.entity.coupon.CouponInfo;
import com.ilhaha.yueyishou.common.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * @Author ilhaha
 * @Create 2024/9/6 08:06
 * @Version 1.0
 */
@FeignClient("service-coupon")
public interface CouponInfoFeignClient {

    /**
     * 通过id集合获取服务抵扣劵集合
     * @param couponIds
     * @return
     */
    @PostMapping("/couponInfo/list/byIds")
    Result<List<CouponInfo>> getListByIds(@RequestBody List<Long> couponIds);

    /**
     * 服务抵扣劵分页查询
     *
     * @param couponInfo
     * @param pageNo
     * @param pageSize
     * @return
     */
    @PostMapping(value = "/couponInfo/list")
    Result<Page<CouponInfo>> queryPageList(@RequestBody CouponInfo couponInfo,
                                           @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                           @RequestParam(name="pageSize", defaultValue="10") Integer pageSize);
    /**
     *   添加
     *
     * @param couponInfo
     * @return
     */
    @PostMapping(value = "/couponInfo/add")
    Result<String> add(@RequestBody CouponInfo couponInfo);

    /**
     *  编辑
     *
     * @param couponInfo
     * @return
     */
    @PutMapping(value = "/couponInfo/edit")
    Result<String> edit(@RequestBody CouponInfo couponInfo);

    /**
     *   通过id删除
     *
     * @param id
     * @return
     */
    @DeleteMapping(value = "/couponInfo/delete")
    Result<String> delete(@RequestParam(name="id",required=true) String id);

    /**
     *  批量删除
     *
     * @param ids
     * @return
     */
    @DeleteMapping(value = "/couponInfo/deleteBatch")
    Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids);

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/couponInfo/queryById")
    Result<CouponInfo> queryById(@RequestParam(name="id",required=true) String id);
}
