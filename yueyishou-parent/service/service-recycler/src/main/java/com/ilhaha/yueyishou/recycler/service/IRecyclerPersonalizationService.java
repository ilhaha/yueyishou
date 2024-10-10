package com.ilhaha.yueyishou.recycler.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ilhaha.yueyishou.model.entity.recycler.RecyclerPersonalization;
import com.ilhaha.yueyishou.model.form.recycler.OrderSettingForm;
import com.ilhaha.yueyishou.model.vo.recycler.OrderSettingVo;

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

    /**
     * 获取回收员的接单设置
     * @param recyclerId
     * @return
     */
    OrderSettingVo orderSetting(Long recyclerId);

    /**
     * 修改回收员接单设置
     * @param orderSettingForm
     * @return
     */
    Boolean updateOrderSetting(OrderSettingForm orderSettingForm);
}
