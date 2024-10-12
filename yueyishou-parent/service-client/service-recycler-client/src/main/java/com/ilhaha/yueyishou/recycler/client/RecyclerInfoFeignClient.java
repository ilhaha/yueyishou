package com.ilhaha.yueyishou.recycler.client;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ilhaha.yueyishou.model.entity.recycler.RecyclerInfo;
import com.ilhaha.yueyishou.model.form.recycler.RecyclerAuthForm;
import com.ilhaha.yueyishou.model.form.recycler.UpdateRecyclerStatusForm;
import com.ilhaha.yueyishou.common.result.Result;
import com.ilhaha.yueyishou.model.vo.recycler.RecyclerAuthImagesVo;
import com.ilhaha.yueyishou.model.vo.recycler.RecyclerBaseInfoVo;
import com.ilhaha.yueyishou.model.vo.recycler.UpdateRecyclerLocationForm;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * @Author ilhaha
 * @Create 2024/9/2 22:46
 * @Version 1.0
 */
@FeignClient("service-recycler")
public interface RecyclerInfoFeignClient {
    /**
     * 以防如果用户还未退出登录就已经认证成功成为回收员出现信息不全问题
     * 也就是redis中无该回收员Id
     * @param customerId
     * @param token
     * @return
     */
    @PostMapping("/recyclerInfo/replenish/info/{customerId}")
    Result<Boolean> replenishInfo(@PathVariable("customerId") Long customerId,
                                         @RequestHeader("token") String token);

    /**
     * 根据顾客Id获取回收员认证图片信息
     * @param customerId
     * @return
     */
    @GetMapping("/recyclerInfo/get/authImages")
    Result<RecyclerAuthImagesVo> getAuthImages(@RequestParam("customerId") Long customerId);


    /**
     *  编辑
     *
     * @param recyclerInfo
     * @return
     */
    @PostMapping("/recyclerInfo/edit")
    Result<String> edit(@RequestBody RecyclerInfo recyclerInfo);

    /**
     * 回收员审核
     * @param recyclerAuthForm
     * @return
     */
    @PostMapping("/recyclerInfo/auth")
    Result<String> auth(@RequestBody RecyclerAuthForm recyclerAuthForm);

    /**
     * 回收员状态切换
     * @param updateRecyclerStatusForm
     * @return
     */
    @PostMapping("/recyclerInfo/switch/status")
    Result<String> switchStatus(@RequestBody UpdateRecyclerStatusForm updateRecyclerStatusForm);

    /**
     * 回收员分页列表查询
     *
     * @param recyclerInfo
     * @param pageNo
     * @param pageSize
     * @return
     */
    @PostMapping(value = "/recyclerInfo/list")
    Result<Page<RecyclerInfo>> queryPageList(@RequestBody RecyclerInfo recyclerInfo,
                                                    @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                    @RequestParam(name="pageSize", defaultValue="10") Integer pageSize);

    /**
     *   通过id删除回收员
     *
     * @param id
     * @return
     */
    @DeleteMapping(value = "/recyclerInfo/delete")
    Result<String> delete(@RequestParam(name="id",required=true) String id);

    /**
     *  批量删除回收员
     *
     * @param ids
     * @return
     */
    @DeleteMapping(value = "/recyclerInfo/deleteBatch")
    Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids);

    /**
     * 根据顾客Id获取回收员认证信息
     * @param customerId
     * @return
     */
    @PostMapping("/recyclerInfo/get/auth")
    Result<RecyclerInfo> getAuth(@RequestParam("customerId") Long customerId);

    /**
     * 添加回收员信息
     * @param recyclerInfo
     * @return
     */
    @PostMapping(value = "/recyclerInfo/add")
    Result<RecyclerInfo> add(@RequestBody RecyclerInfo recyclerInfo);


    /**
     * 获取回收员基本信息
     * @param recyclerId
     * @return
     */
    @GetMapping("/recyclerInfo/base/info/{recyclerId}")
    Result<RecyclerBaseInfoVo> getBaseInfo(@PathVariable("recyclerId") Long recyclerId);
}
