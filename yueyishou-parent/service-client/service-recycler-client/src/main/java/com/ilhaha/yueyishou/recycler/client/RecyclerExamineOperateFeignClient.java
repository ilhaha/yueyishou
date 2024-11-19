package com.ilhaha.yueyishou.recycler.client;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ilhaha.yueyishou.common.result.Result;
import com.ilhaha.yueyishou.model.entity.recycler.RecyclerExamineOperate;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Author ilhaha
 * @Create 2024/11/18 17:12
 * @Version 1.0
 */
@FeignClient("service-recycler")
public interface RecyclerExamineOperateFeignClient {

    /**
     * 获取回收员认证审批操作记录列表
     *
     * @param pageNo
     * @param pageSize
     * @param recyclerId
     * @return
     */
    @GetMapping("/recyclerExamineOperate/list")
    Result<Page<RecyclerExamineOperate>> getRecyclerExamineOperateList(@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                                              @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                                              @RequestParam("recyclerId") Long recyclerId);
    /**
     * 添加回收员认证审批操作记录
     * @param recyclerExamineOperate
     * @return
     */
    @PostMapping("/recyclerExamineOperate/add")
    Result<Boolean> add(@RequestBody RecyclerExamineOperate recyclerExamineOperate);
}