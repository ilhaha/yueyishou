package org.jeecg.modules.mgr.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ilhaha.yueyishou.model.entity.recycler.RecyclerAccountOperate;
import com.ilhaha.yueyishou.model.entity.recycler.RecyclerExamineOperate;
import com.ilhaha.yueyishou.model.entity.recycler.RecyclerInfo;
import com.ilhaha.yueyishou.model.form.recycler.RecyclerAuthForm;
import com.ilhaha.yueyishou.model.form.recycler.UpdateRecyclerStatusForm;
import com.ilhaha.yueyishou.recycler.client.RecyclerAccountOperateFeignClient;
import com.ilhaha.yueyishou.recycler.client.RecyclerExamineOperateFeignClient;
import com.ilhaha.yueyishou.recycler.client.RecyclerInfoFeignClient;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.mgr.service.RecyclerService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author ilhaha
 * @Create 2024/9/8 11:50
 * @Version 1.0
 */
@Service
public class RecyclerServiceImpl implements RecyclerService {

    @Resource
    private RecyclerInfoFeignClient recyclerInfoFeignClient;

    @Resource
    private RecyclerExamineOperateFeignClient recyclerExamineOperateFeignClient;

    @Resource
    private RecyclerAccountOperateFeignClient recyclerAccountOperateFeignClient;

    /**
     * 回收员分页列表查询
     *
     * @param recyclerInfo
     * @param pageNo
     * @param pageSize
     * @return
     */
    @Override
    public Page<RecyclerInfo> queryPageList(RecyclerInfo recyclerInfo, Integer pageNo, Integer pageSize) {
        return recyclerInfoFeignClient.queryPageList(recyclerInfo,pageNo,pageSize).getData();
    }

    /**
     * 回收员审核
     * @param recyclerAuthForm
     * @return
     */
    @Override
    public String auth(RecyclerAuthForm recyclerAuthForm) {
        return recyclerInfoFeignClient.auth(recyclerAuthForm).getData();
    }

    /**
     * 回收员状态切换
     * @param updateRecyclerStatusForm
     * @return
     */
    @Override
    public String switchStatus(UpdateRecyclerStatusForm updateRecyclerStatusForm) {
        return recyclerInfoFeignClient.switchStatus(updateRecyclerStatusForm).getData();
    }

    /**
     *   通过id删除回收员
     *
     * @param id
     * @return
     */
    @Override
    public String delete(String id) {
        return recyclerInfoFeignClient.delete(id).getData();
    }

    /**
     *  批量删除回收员
     *
     * @param ids
     * @return
     */
    @Override
    public String deleteBatch(String ids) {
        return recyclerInfoFeignClient.deleteBatch(ids).getData();
    }

    /**
     * 添加回收员认证审批操作记录
     * @param recyclerExamineOperate
     * @return
     */
    @Override
    public Boolean add(RecyclerExamineOperate recyclerExamineOperate) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        recyclerExamineOperate.setOperator(sysUser.getRealname());
        return recyclerExamineOperateFeignClient.add(recyclerExamineOperate).getData();
    }

    /**
     * 获取回收员认证审批操作记录列表
     *
     * @param pageNo
     * @param pageSize
     * @param recyclerId
     * @return
     */
    @Override
    public Page<RecyclerExamineOperate> getRecyclerExamineOperateList(Integer pageNo, Integer pageSize, Long recyclerId) {
        return recyclerExamineOperateFeignClient.getRecyclerExamineOperateList(pageNo,pageSize,recyclerId).getData();
    }

    /**
     * 添加回收员账号操作记录信息
     * @param recyclerAccountOperate
     * @return
     */
    @Override
    public Boolean add(RecyclerAccountOperate recyclerAccountOperate) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        recyclerAccountOperate.setOperator(sysUser.getRealname());
        return recyclerAccountOperateFeignClient.add(recyclerAccountOperate).getData();
    }

    /**
     * 获取回收员账号操作记录列表
     *
     * @param pageNo
     * @param pageSize
     * @param recyclerId
     * @return
     */
    @Override
    public Page<RecyclerAccountOperate> getRecyclerAccountOperateList(Integer pageNo, Integer pageSize, Long recyclerId) {
        return recyclerAccountOperateFeignClient.getRecyclerAccountOperateList(pageNo,pageSize,recyclerId).getData();
    }
}
