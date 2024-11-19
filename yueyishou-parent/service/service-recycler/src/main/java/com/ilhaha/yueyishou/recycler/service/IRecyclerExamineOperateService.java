package com.ilhaha.yueyishou.recycler.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ilhaha.yueyishou.model.entity.recycler.RecyclerExamineOperate;

/**
 * @Author ilhaha
 * @Create 2024/11/18 17:07
 * @Version 1.0
 */
public interface IRecyclerExamineOperateService extends IService<RecyclerExamineOperate> {

    /**
     * 添加回收员认证审批操作记录
     * @param recyclerExamineOperate
     * @return
     */
    Boolean add(RecyclerExamineOperate recyclerExamineOperate);

    /**
     * 获取回收员认证审批操作记录列表
     * @param page
     * @param recyclerId
     * @return
     */
    Page<RecyclerExamineOperate> getRecyclerExamineOperateList(Page<RecyclerExamineOperate> page, Long recyclerId);
}
