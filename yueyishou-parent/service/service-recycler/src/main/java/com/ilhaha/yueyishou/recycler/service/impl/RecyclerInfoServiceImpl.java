package com.ilhaha.yueyishou.recycler.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ilhaha.yueyishou.entity.recycler.RecyclerInfo;
import com.ilhaha.yueyishou.recycler.mapper.RecyclerInfoMapper;
import com.ilhaha.yueyishou.recycler.service.IRecyclerInfoService;
import org.springframework.stereotype.Service;

@Service
public class RecyclerInfoServiceImpl extends ServiceImpl<RecyclerInfoMapper, RecyclerInfo> implements IRecyclerInfoService {

}
