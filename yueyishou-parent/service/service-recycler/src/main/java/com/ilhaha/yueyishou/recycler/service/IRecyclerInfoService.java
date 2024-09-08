package com.ilhaha.yueyishou.recycler.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ilhaha.yueyishou.entity.recycler.RecyclerInfo;
import com.ilhaha.yueyishou.form.recycler.RecyclerAuthForm;
import com.ilhaha.yueyishou.form.recycler.UpdateRecyclerStatusForm;

public interface IRecyclerInfoService extends IService<RecyclerInfo> {

    /**
     * 回收员状态切换
     * @param updateRecyclerStatusForm
     * @return
     */
    String switchStatus(UpdateRecyclerStatusForm updateRecyclerStatusForm);

    /**
     * 回收员审核
     * @param recyclerAuthForm
     * @return
     */
    String auth(RecyclerAuthForm recyclerAuthForm);
}
