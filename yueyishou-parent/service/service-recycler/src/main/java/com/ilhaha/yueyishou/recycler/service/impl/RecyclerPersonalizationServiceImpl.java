package com.ilhaha.yueyishou.recycler.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
        LambdaUpdateWrapper<RecyclerPersonalization> recyclerPersonalizationLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        recyclerPersonalizationLambdaUpdateWrapper.eq(RecyclerPersonalization::getRecyclerId,recyclerId);
        return this.getOne(recyclerPersonalizationLambdaUpdateWrapper);
    }

}
