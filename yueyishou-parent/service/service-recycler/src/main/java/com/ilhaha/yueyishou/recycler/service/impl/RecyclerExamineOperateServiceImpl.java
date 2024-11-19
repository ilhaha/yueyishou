package com.ilhaha.yueyishou.recycler.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ilhaha.yueyishou.model.entity.recycler.RecyclerExamineOperate;
import com.ilhaha.yueyishou.recycler.mapper.RecyclerExamineOperateMapper;
import com.ilhaha.yueyishou.recycler.service.IRecyclerExamineOperateService;
import org.springframework.stereotype.Service;

/**
 * @Author ilhaha
 * @Create 2024/11/18 17:07
 * @Version 1.0
 */
@Service
public class RecyclerExamineOperateServiceImpl extends ServiceImpl<RecyclerExamineOperateMapper, RecyclerExamineOperate> implements IRecyclerExamineOperateService {

    /**
     * 添加回收员认证审批操作记录
     * @param recyclerExamineOperate
     * @return
     */
    @Override
    public Boolean add(RecyclerExamineOperate recyclerExamineOperate) {
        return this.save(recyclerExamineOperate);
    }

    /**
     * 获取回收员认证审批操作记录列表
     * @param page
     * @param recyclerId
     * @return
     */
    @Override
    public Page<RecyclerExamineOperate> getRecyclerExamineOperateList(Page<RecyclerExamineOperate> page, Long recyclerId) {
        LambdaQueryWrapper<RecyclerExamineOperate> recyclerExamineOperateLambdaQueryWrapper = new LambdaQueryWrapper<>();
        recyclerExamineOperateLambdaQueryWrapper.eq(RecyclerExamineOperate::getRecyclerId,recyclerId)
                .orderByDesc(RecyclerExamineOperate::getCreateTime);
        return this.page(page,recyclerExamineOperateLambdaQueryWrapper);
    }
}
