package com.ilhaha.yueyishou.recycler.service;

import com.ilhaha.yueyishou.common.result.Result;
import com.ilhaha.yueyishou.model.form.order.MatchingOrderForm;
import com.ilhaha.yueyishou.model.vo.order.MatchingOrderVo;
import com.ilhaha.yueyishou.model.vo.recycler.RecyclerBaseInfoVo;
import com.ilhaha.yueyishou.model.vo.recycler.UpdateRecyclerLocationForm;
import com.ilhaha.yueyishou.model.vo.tencentcloud.CosUploadVo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @Author ilhaha
 * @Create 2024/9/8 17:20
 * @Version 1.0
 */
public interface RecyclerService {

    /**
     * 获取回收员基本信息
     * @return
     */
    Result<RecyclerBaseInfoVo> getBaseInfo();

    /**
     * 上传回收员实时位置
     * @param updateRecyclerLocationForm
     * @return
     */
    Result<Boolean> updateRecyclerLocation(UpdateRecyclerLocationForm updateRecyclerLocationForm);

    /**
     * 删除回收员位置信息
     * @return
     */
    Result<Boolean> removeRecyclerLocation();


}
