package com.ilhaha.yueyishou.recycler.controller;

import com.ilhaha.yueyishou.common.anno.LoginVerification;
import com.ilhaha.yueyishou.model.form.order.MatchingOrderForm;
import com.ilhaha.yueyishou.model.vo.order.MatchingOrderVo;
import com.ilhaha.yueyishou.model.vo.recycler.RecyclerBaseInfoVo;
import com.ilhaha.yueyishou.model.vo.recycler.UpdateRecyclerLocationForm;
import com.ilhaha.yueyishou.recycler.service.RecyclerService;
import com.ilhaha.yueyishou.common.result.Result;
import com.ilhaha.yueyishou.model.vo.tencentcloud.CosUploadVo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @Author ilhaha
 * @Create 2024/9/8 17:18
 * @Version 1.0
 */
@RestController
@RequestMapping("/recycler")
public class RecyclerController {

    @Resource
    private RecyclerService recyclerService;


    /**
     * 删除回收员位置信息
     * @return
     */
    @LoginVerification
    @PostMapping("/removeRecyclerLocation")
    public Result<Boolean> removeDriverLocation() {
        return recyclerService.removeRecyclerLocation();
    }

    /**
     * 上传回收员实时位置
     * @param updateRecyclerLocationForm
     * @return
     */
    @LoginVerification
    @PostMapping("/updateRecyclerLocation")
    public Result<Boolean> updateRecyclerLocation(@RequestBody UpdateRecyclerLocationForm updateRecyclerLocationForm) {
        return recyclerService.updateRecyclerLocation(updateRecyclerLocationForm);
    }

    /**
     * 获取回收员基本信息
     * @param
     * @return
     */
    @LoginVerification
    @GetMapping("/base/info")
    public Result<RecyclerBaseInfoVo> getBaseInfo(){
        return recyclerService.getBaseInfo();
    }
}
