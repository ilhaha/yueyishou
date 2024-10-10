package com.ilhaha.yueyishou.recycler.service.impl;

import com.ilhaha.yueyishou.common.anno.LoginVerification;
import com.ilhaha.yueyishou.common.result.Result;
import com.ilhaha.yueyishou.common.util.AuthContextHolder;
import com.ilhaha.yueyishou.model.entity.recycler.RecyclerPersonalization;
import com.ilhaha.yueyishou.model.form.recycler.OrderSettingForm;
import com.ilhaha.yueyishou.model.vo.recycler.OrderSettingVo;
import com.ilhaha.yueyishou.recycler.client.RecyclerPersonalizationFeignClient;
import com.ilhaha.yueyishou.recycler.service.PersonalizationService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * @Author ilhaha
 * @Create 2024/9/26 15:09
 * @Version 1.0
 */
@Service
public class PersonalizationServiceImpl implements PersonalizationService {

    @Resource
    private RecyclerPersonalizationFeignClient recyclerPersonalizationFeignClient;

    /**
     * 根据回收员ID获取回收员的个性化设置
     * @return
     */
    @LoginVerification
    @Override
    public Result<RecyclerPersonalization> getPersonalizationByRecyclerId() {
        return recyclerPersonalizationFeignClient.getPersonalizationByRecyclerId(AuthContextHolder.getRecyclerId());
    }

    /**
     * 修改回收员服务状态
     * @param serviceStatus
     * @return
     */
    @Override
    public Result<Boolean> takeOrders(Integer serviceStatus) {

        return recyclerPersonalizationFeignClient.takeOrders(AuthContextHolder.getRecyclerId(),serviceStatus);
    }

    /**
     * 获取回收员的接单设置
     * @return
     */
    @Override
    public Result<OrderSettingVo> orderSetting() {
        return recyclerPersonalizationFeignClient.orderSetting(AuthContextHolder.getRecyclerId());
    }

    /**
     * 修改回收员接单设置
     * @param orderSettingForm
     * @return
     */
    @Override
    public Result<Boolean> updateOrderSetting(OrderSettingForm orderSettingForm) {
        orderSettingForm.setRecyclerId(AuthContextHolder.getRecyclerId());
        return recyclerPersonalizationFeignClient.updateOrderSetting(orderSettingForm);
    }
}
