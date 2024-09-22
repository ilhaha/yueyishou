package org.jeecg.modules.mgr.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ilhaha.yueyishou.model.entity.recycler.RecyclerInfo;
import com.ilhaha.yueyishou.model.form.recycler.RecyclerAuthForm;
import com.ilhaha.yueyishou.model.form.recycler.UpdateRecyclerStatusForm;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.mgr.service.RecyclerService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @Author ilhaha
 * @Create 2024/9/8 11:50
 * @Version 1.0
 */
@RestController
@RequestMapping("/recycler")
public class RecyclerController {

    @Resource
    private RecyclerService recyclerService;

    /**
     * 回收员审核
     * @param recyclerAuthForm
     * @return
     */
    @PostMapping("/auth")
    public Result<String> auth(@RequestBody RecyclerAuthForm recyclerAuthForm) {
        return Result.OK(recyclerService.auth(recyclerAuthForm));
    }


    /**
     * 回收员状态切换
     * @param updateRecyclerStatusForm
     * @return
     */
    @PostMapping("/switch/status")
    public Result<String> switchStatus(@RequestBody UpdateRecyclerStatusForm updateRecyclerStatusForm){
        return Result.OK(recyclerService.switchStatus(updateRecyclerStatusForm));
    }


    /**
     * 回收员分页列表查询
     *
     * @param recyclerInfo
     * @param pageNo
     * @param pageSize
     * @return
     */
    @GetMapping(value = "/list")
    public Result<Page<RecyclerInfo>> queryPageList(RecyclerInfo recyclerInfo,
                                             @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                             @RequestParam(name="pageSize", defaultValue="10") Integer pageSize){
        return Result.OK(recyclerService.queryPageList(recyclerInfo,pageNo,pageSize));
    }

    /**
     *   通过id删除回收员
     *
     * @param id
     * @return
     */
    @DeleteMapping(value = "/delete")
    public Result<String> delete(@RequestParam(name="id",required=true) String id) {
        return Result.OK(recyclerService.delete(id));
    }

    /**
     *  批量删除回收员
     *
     * @param ids
     * @return
     */
    @DeleteMapping(value = "/deleteBatch")
    public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
        return Result.OK(recyclerService.deleteBatch(ids));
    }
}
