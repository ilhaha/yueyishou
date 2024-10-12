package com.ilhaha.yueyishou.recycler.service.impl;

import com.ilhaha.yueyishou.client.LocationFeignClient;
import com.ilhaha.yueyishou.common.util.AuthContextHolder;
import com.ilhaha.yueyishou.model.form.order.MatchingOrderForm;
import com.ilhaha.yueyishou.model.vo.order.MatchingOrderVo;
import com.ilhaha.yueyishou.model.vo.recycler.RecyclerBaseInfoVo;
import com.ilhaha.yueyishou.model.vo.recycler.UpdateRecyclerLocationForm;
import com.ilhaha.yueyishou.recycler.client.RecyclerInfoFeignClient;
import com.ilhaha.yueyishou.recycler.service.RecyclerService;
import com.ilhaha.yueyishou.common.result.Result;
import com.ilhaha.yueyishou.tencentcloud.client.CosFeignClient;
import com.ilhaha.yueyishou.model.vo.tencentcloud.CosUploadVo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @Author ilhaha
 * @Create 2024/9/8 17:20
 * @Version 1.0
 */
@Service
public class RecyclerServiceImpl implements RecyclerService {

    @Resource
    private LocationFeignClient locationFeignClient;

    @Resource
    private RecyclerInfoFeignClient recyclerInfoFeignClient;



    /**
     * 获取回收员基本信息
     * @return
     */
    @Override
    public Result<RecyclerBaseInfoVo> getBaseInfo() {
        return recyclerInfoFeignClient.getBaseInfo(AuthContextHolder.getRecyclerId());
    }

    /**
     * 上传回收员实时位置
     * @param updateRecyclerLocationForm
     * @return
     */
    @Override
    public Result<Boolean> updateRecyclerLocation(UpdateRecyclerLocationForm updateRecyclerLocationForm) {
        updateRecyclerLocationForm.setRecyclerId(AuthContextHolder.getRecyclerId());
        return locationFeignClient.updateRecyclerLocation(updateRecyclerLocationForm);
    }

    /**
     * 删除回收员位置信息
     * @return
     */
    @Override
    public Result<Boolean> removeRecyclerLocation() {
        return locationFeignClient.removeDriverLocation(AuthContextHolder.getRecyclerId());
    }

}
