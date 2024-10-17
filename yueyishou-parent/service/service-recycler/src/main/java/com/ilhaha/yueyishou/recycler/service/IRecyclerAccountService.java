package com.ilhaha.yueyishou.recycler.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ilhaha.yueyishou.model.entity.recycler.RecyclerAccount;

import java.util.List;

public interface IRecyclerAccountService extends IService<RecyclerAccount> {

    /**
     * 批量给回收员创建账户
     * @param recyclerIds
     */
    void createAccount(List<Long> recyclerIds);
}
