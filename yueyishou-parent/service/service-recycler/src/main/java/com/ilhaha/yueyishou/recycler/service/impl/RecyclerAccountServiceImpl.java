package com.ilhaha.yueyishou.recycler.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ilhaha.yueyishou.model.entity.recycler.RecyclerAccount;
import com.ilhaha.yueyishou.recycler.mapper.RecyclerAccountMapper;
import com.ilhaha.yueyishou.recycler.service.IRecyclerAccountService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecyclerAccountServiceImpl extends ServiceImpl<RecyclerAccountMapper, RecyclerAccount> implements IRecyclerAccountService {

    /**
     * 批量给回收员创建账户
     * @param recyclerIds
     */
    @Override
    public void createAccount(List<Long> recyclerIds) {
       this.saveBatch( recyclerIds.stream().map(item -> {
           RecyclerAccount recyclerAccount = new RecyclerAccount();
           recyclerAccount.setRecyclerId(item);
           return recyclerAccount;
       }).collect(Collectors.toList()));
    }
}
