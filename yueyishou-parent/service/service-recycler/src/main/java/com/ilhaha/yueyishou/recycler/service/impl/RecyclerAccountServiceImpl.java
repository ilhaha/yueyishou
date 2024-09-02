package com.ilhaha.yueyishou.recycler.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ilhaha.yueyishou.entity.recycler.RecyclerAccount;
import com.ilhaha.yueyishou.recycler.mapper.RecyclerAccountMapper;
import com.ilhaha.yueyishou.recycler.service.IRecyclerAccountService;
import org.springframework.stereotype.Service;

@Service
public class RecyclerAccountServiceImpl extends ServiceImpl<RecyclerAccountMapper, RecyclerAccount> implements IRecyclerAccountService {

}
