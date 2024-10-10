package com.ilhaha.yueyishou.recycler.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ilhaha.yueyishou.model.constant.PersonalizationConstant;
import com.ilhaha.yueyishou.model.entity.recycler.RecyclerPersonalization;
import com.ilhaha.yueyishou.recycler.mapper.RecyclerPersonalizationMapper;
import com.ilhaha.yueyishou.recycler.service.IRecyclerPersonalizationService;
import org.springframework.stereotype.Service;

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

}
