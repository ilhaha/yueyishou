package com.ilhaha.yueyishou.recycler.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ilhaha.yueyishou.model.entity.recycler.RecyclerPersonalization;

public interface IRecyclerPersonalizationService extends IService<RecyclerPersonalization> {

    /**
     * 根据回收员ID获取回收员的个性化设置
     * @param recyclerId
     * @return
     */
    RecyclerPersonalization getPersonalizationByRecyclerId(Long recyclerId);

    /**
     * 修改回收员服务状态
     * @param recyclerId
     * @param serviceStatus
     * @return
     */
    Boolean takeOrders(Long recyclerId, Integer serviceStatus);
}
