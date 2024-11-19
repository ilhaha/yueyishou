package com.ilhaha.yueyishou.recycler.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ilhaha.yueyishou.common.result.Result;
import com.ilhaha.yueyishou.model.entity.customer.CustomerAccountOperate;
import com.ilhaha.yueyishou.model.entity.recycler.RecyclerExamineOperate;
import com.ilhaha.yueyishou.recycler.service.IRecyclerExamineOperateService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

/**
 * @Author ilhaha
 * @Create 2024/11/18 17:06
 * @Version 1.0
 */
@RestController
@RequestMapping("/recyclerExamineOperate")
public class RecyclerExamineOperateController {

    @Resource
    private IRecyclerExamineOperateService recyclerExamineOperateService;


    /**
     * 获取回收员认证审批操作记录列表
     *
     * @param pageNo
     * @param pageSize
     * @param recyclerId
     * @return
     */
    @GetMapping("/list")
    public Result<Page<RecyclerExamineOperate>> getRecyclerExamineOperateList(@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                                              @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                                              @RequestParam("recyclerId") Long recyclerId) {
        Page<RecyclerExamineOperate> page = new Page<>(pageNo, pageSize);
        return Result.ok(recyclerExamineOperateService.getRecyclerExamineOperateList(page,recyclerId));
    }

    /**
     * 添加回收员认证审批操作记录
     *
     * @param recyclerExamineOperate
     * @return
     */
    @PostMapping("/add")
    public Result<Boolean> add(@RequestBody RecyclerExamineOperate recyclerExamineOperate) {
        return Result.ok(recyclerExamineOperateService.add(recyclerExamineOperate));
    }


}
