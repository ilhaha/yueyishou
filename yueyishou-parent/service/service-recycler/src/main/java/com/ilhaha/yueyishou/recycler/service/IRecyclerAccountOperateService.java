package com.ilhaha.yueyishou.recycler.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ilhaha.yueyishou.model.entity.recycler.RecyclerAccountOperate;

/**
 * @Author ilhaha
 * @Create 2024/11/18 18:48
 * @Version 1.0
 */
public interface IRecyclerAccountOperateService extends IService<RecyclerAccountOperate> {

    /**
     * 添加回收员账号操作记录信息
     * @param recyclerAccountOperate
     * @return
     */
    Boolean add(RecyclerAccountOperate recyclerAccountOperate);


    /**
     * 获取回收员账号操作记录列表
     * @param page
     * @param recyclerId
     * @return
     */
    Page<RecyclerAccountOperate> getRecyclerAccountOperateList(Page<RecyclerAccountOperate> page, Long recyclerId);

}
