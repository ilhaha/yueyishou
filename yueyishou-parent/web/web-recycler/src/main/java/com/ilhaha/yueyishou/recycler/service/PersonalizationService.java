package com.ilhaha.yueyishou.recycler.service;

import com.ilhaha.yueyishou.common.result.Result;
import com.ilhaha.yueyishou.model.entity.recycler.RecyclerPersonalization;
import com.ilhaha.yueyishou.model.form.recycler.OrderSettingForm;
import com.ilhaha.yueyishou.model.vo.recycler.OrderSettingVo;

/**
 * @Author ilhaha
 * @Create 2024/9/26 15:09
 * @Version 1.0
 */
public interface PersonalizationService {

    /**
     * 根据回收员ID获取回收员的个性化设置
     * @return
     */
    Result<RecyclerPersonalization> getPersonalizationByRecyclerId();

    /**
     * 修改回收员服务状态
     * @param serviceStatus
     * @return
     */
    Result<Boolean> takeOrders(Integer serviceStatus);

    /**
     * 获取回收员的接单设置
     * @return
     */
    Result<OrderSettingVo> orderSetting();

    /**
     * 修改回收员接单设置
     * @param orderSettingForm
     * @return
     */
    Result<Boolean> updateOrderSetting(OrderSettingForm orderSettingForm);
}
