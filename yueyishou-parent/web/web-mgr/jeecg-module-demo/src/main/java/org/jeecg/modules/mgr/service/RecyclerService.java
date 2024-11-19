package org.jeecg.modules.mgr.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ilhaha.yueyishou.model.entity.recycler.RecyclerAccountOperate;
import com.ilhaha.yueyishou.model.entity.recycler.RecyclerExamineOperate;
import com.ilhaha.yueyishou.model.entity.recycler.RecyclerInfo;
import com.ilhaha.yueyishou.model.form.recycler.RecyclerAuthForm;
import com.ilhaha.yueyishou.model.form.recycler.UpdateRecyclerStatusForm;

/**
 * @Author ilhaha
 * @Create 2024/9/8 11:50
 * @Version 1.0
 */
public interface RecyclerService {

    /**
     * 回收员分页列表查询
     *
     * @param recyclerInfo
     * @param pageNo
     * @param pageSize
     * @return
     */
    Page<RecyclerInfo> queryPageList(RecyclerInfo recyclerInfo, Integer pageNo, Integer pageSize);

    /**
     * 回收员审核
     * @param recyclerAuthForm
     * @return
     */
    String auth(RecyclerAuthForm recyclerAuthForm);

    /**
     * 回收员状态切换
     * @param updateRecyclerStatusForm
     * @return
     */
    String switchStatus(UpdateRecyclerStatusForm updateRecyclerStatusForm);

    /**
     *   通过id删除回收员
     *
     * @param id
     * @return
     */
    String delete(String id);

    /**
     *  批量删除回收员
     *
     * @param ids
     * @return
     */
    String deleteBatch(String ids);

    /**
     * 添加回收员认证审批操作记录
     * @param recyclerExamineOperate
     * @return
     */
    Boolean add(RecyclerExamineOperate recyclerExamineOperate);

    /**
     * 获取回收员认证审批操作记录列表
     *
     * @param pageNo
     * @param pageSize
     * @param recyclerId
     * @return
     */
    Page<RecyclerExamineOperate> getRecyclerExamineOperateList(Integer pageNo, Integer pageSize, Long recyclerId);

    /**
     * 添加回收员账号操作记录信息
     * @param recyclerAccountOperate
     * @return
     */
    Boolean add(RecyclerAccountOperate recyclerAccountOperate);

    /**
     * 获取回收员账号操作记录列表
     *
     * @param pageNo
     * @param pageSize
     * @param recyclerId
     * @return
     */
    Page<RecyclerAccountOperate> getRecyclerAccountOperateList(Integer pageNo, Integer pageSize, Long recyclerId);
}
