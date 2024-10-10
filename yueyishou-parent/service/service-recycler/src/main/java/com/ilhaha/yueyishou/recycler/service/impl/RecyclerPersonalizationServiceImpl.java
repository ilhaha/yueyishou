package com.ilhaha.yueyishou.recycler.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ilhaha.yueyishou.model.constant.PersonalizationConstant;
import com.ilhaha.yueyishou.model.entity.recycler.RecyclerPersonalization;
import com.ilhaha.yueyishou.model.form.recycler.OrderSettingForm;
import com.ilhaha.yueyishou.model.vo.recycler.OrderSettingVo;
import com.ilhaha.yueyishou.recycler.mapper.RecyclerPersonalizationMapper;
import com.ilhaha.yueyishou.recycler.service.IRecyclerPersonalizationService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Service
public class RecyclerPersonalizationServiceImpl extends ServiceImpl<RecyclerPersonalizationMapper, RecyclerPersonalization> implements IRecyclerPersonalizationService {

    /**
     * 根据回收员ID获取回收员的个性化设置
     * @param recyclerId
     * @return
     */
    @Override
    public RecyclerPersonalization getPersonalizationByRecyclerId(Long recyclerId) {
        LambdaQueryWrapper<RecyclerPersonalization> recyclerPersonalizationLambdaUpdateWrapper = new LambdaQueryWrapper<>();
        recyclerPersonalizationLambdaUpdateWrapper.eq(RecyclerPersonalization::getRecyclerId,recyclerId);
        return this.getOne(recyclerPersonalizationLambdaUpdateWrapper);
    }

    /**
     * 修改回收员服务状态
     * @param recyclerId
     * @param serviceStatus
     * @return
     */
    @Override
    public Boolean takeOrders(Long recyclerId, Integer serviceStatus) {
        LambdaUpdateWrapper<RecyclerPersonalization> recyclerPersonalizationLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        recyclerPersonalizationLambdaUpdateWrapper.set(RecyclerPersonalization::getServiceStatus, serviceStatus)
                .eq(RecyclerPersonalization::getRecyclerId,recyclerId);
        return this.update(recyclerPersonalizationLambdaUpdateWrapper);
    }

    /**
     * 获取回收员的接单设置
     * @param recyclerId
     * @return
     */
    @Override
    public OrderSettingVo orderSetting(Long recyclerId) {
        LambdaQueryWrapper<RecyclerPersonalization> recyclerPersonalizationLambdaQueryWrapper = new LambdaQueryWrapper<>();
        recyclerPersonalizationLambdaQueryWrapper.eq(RecyclerPersonalization::getRecyclerId,recyclerId);
        RecyclerPersonalization recyclerPersonalizationDB = this.getOne(recyclerPersonalizationLambdaQueryWrapper);
        OrderSettingVo orderSettingVo = new OrderSettingVo();
        BeanUtils.copyProperties(recyclerPersonalizationDB,orderSettingVo);
        return orderSettingVo;
    }

    /**
     * 修改回收员接单设置
     * @param orderSettingForm
     * @return
     */
    @Override
    public Boolean updateOrderSetting(OrderSettingForm orderSettingForm) {
        LambdaUpdateWrapper<RecyclerPersonalization> recyclerPersonalizationLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        recyclerPersonalizationLambdaUpdateWrapper.set(!ObjectUtils.isEmpty(orderSettingForm.getRecyclingType()),RecyclerPersonalization::getRecyclingType,orderSettingForm.getRecyclingType())
                .set(!ObjectUtils.isEmpty(orderSettingForm.getAcceptDistance()),RecyclerPersonalization::getAcceptDistance,orderSettingForm.getAcceptDistance())
                .eq(RecyclerPersonalization::getRecyclerId,orderSettingForm.getRecyclerId());
        return this.update(recyclerPersonalizationLambdaUpdateWrapper);
    }

}
