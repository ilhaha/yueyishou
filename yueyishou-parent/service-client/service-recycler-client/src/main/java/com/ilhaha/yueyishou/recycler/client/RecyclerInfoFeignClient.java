package com.ilhaha.yueyishou.recycler.client;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ilhaha.yueyishou.entity.recycler.RecyclerInfo;
import com.ilhaha.yueyishou.form.recycler.RecyclerAuthForm;
import com.ilhaha.yueyishou.form.recycler.UpdateRecyclerStatusForm;
import com.ilhaha.yueyishou.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;

/**
 * @Author ilhaha
 * @Create 2024/9/2 22:46
 * @Version 1.0
 */
@FeignClient("service-recycler")
public interface RecyclerInfoFeignClient {

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
}
