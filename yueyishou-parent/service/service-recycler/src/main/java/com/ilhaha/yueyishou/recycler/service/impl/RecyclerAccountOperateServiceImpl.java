package com.ilhaha.yueyishou.recycler.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ilhaha.yueyishou.model.entity.recycler.RecyclerAccountOperate;
import com.ilhaha.yueyishou.recycler.mapper.RecyclerAccountOperateMapper;
import com.ilhaha.yueyishou.recycler.service.IRecyclerAccountOperateService;
import org.springframework.stereotype.Service;

/**
 * @Author ilhaha
 * @Create 2024/11/18 18:48
 * @Version 1.0
 */
@Service
public class RecyclerAccountOperateServiceImpl extends ServiceImpl<RecyclerAccountOperateMapper, RecyclerAccountOperate> implements IRecyclerAccountOperateService {

    /**
     * 添加回收员账号操作记录信息
     * @param recyclerAccountOperate
     * @return
     */
    @Override
    public Boolean add(RecyclerAccountOperate recyclerAccountOperate) {
        return this.save(recyclerAccountOperate);
    }

    /**
     * 获取回收员账号操作记录列表
     * @param page
     * @param recyclerId
     * @return
     */
    @Override
    public Page<RecyclerAccountOperate> getRecyclerAccountOperateList(Page<RecyclerAccountOperate> page, Long recyclerId) {
        LambdaQueryWrapper<RecyclerAccountOperate> recyclerAccountOperateLambdaQueryWrapper = new LambdaQueryWrapper<>();
        recyclerAccountOperateLambdaQueryWrapper.eq(RecyclerAccountOperate::getRecyclerId,recyclerId)
                .orderByDesc(RecyclerAccountOperate::getCreateTime);
        return this.page(page,recyclerAccountOperateLambdaQueryWrapper);
    }
}
