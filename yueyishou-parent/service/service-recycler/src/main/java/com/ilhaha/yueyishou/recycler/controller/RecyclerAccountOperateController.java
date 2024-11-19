package com.ilhaha.yueyishou.recycler.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ilhaha.yueyishou.common.result.Result;
import com.ilhaha.yueyishou.model.entity.recycler.RecyclerAccountOperate;
import com.ilhaha.yueyishou.model.entity.recycler.RecyclerExamineOperate;
import com.ilhaha.yueyishou.recycler.service.IRecyclerAccountOperateService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

/**
 * @Author ilhaha
 * @Create 2024/11/18 18:46
 * @Version 1.0
 */
@RestController
@RequestMapping("/recyclerAccountOperate")
public class RecyclerAccountOperateController {

    @Resource
    private IRecyclerAccountOperateService recyclerAccountOperateService;

    /**
     * 获取回收员账号操作记录列表
     *
     * @param pageNo
     * @param pageSize
     * @param recyclerId
     * @return
     */
    @GetMapping("/list")
    public Result<Page<RecyclerAccountOperate>> getRecyclerAccountOperateList(@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                                              @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                                              @RequestParam("recyclerId") Long recyclerId) {
        Page<RecyclerAccountOperate> page = new Page<>(pageNo, pageSize);
        return Result.ok(recyclerAccountOperateService.getRecyclerAccountOperateList(page,recyclerId));
    }

    /**
     * 添加回收员账号操作记录信息
     * @param recyclerAccountOperate
     * @return
     */
    @PostMapping("/add")
    public Result<Boolean> add(@RequestBody RecyclerAccountOperate recyclerAccountOperate){
        return Result.ok(recyclerAccountOperateService.add(recyclerAccountOperate));
    }
}
