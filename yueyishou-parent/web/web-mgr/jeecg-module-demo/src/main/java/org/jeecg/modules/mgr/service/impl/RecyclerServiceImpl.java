package org.jeecg.modules.mgr.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ilhaha.yueyishou.entity.recycler.RecyclerInfo;
import com.ilhaha.yueyishou.form.recycler.RecyclerAuthForm;
import com.ilhaha.yueyishou.form.recycler.UpdateRecyclerStatusForm;
import com.ilhaha.yueyishou.recycler.client.RecyclerInfoFeignClient;
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
}
