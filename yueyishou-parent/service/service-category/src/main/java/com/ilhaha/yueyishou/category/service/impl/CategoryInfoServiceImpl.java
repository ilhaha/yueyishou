package com.ilhaha.yueyishou.category.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ilhaha.yueyishou.entity.category.CategoryInfo;
import com.ilhaha.yueyishou.category.mapper.CategoryInfoMapper;
import com.ilhaha.yueyishou.category.service.ICategoryInfoService;
import org.springframework.stereotype.Service;

@Service
public class CategoryInfoServiceImpl extends ServiceImpl<CategoryInfoMapper, CategoryInfo> implements ICategoryInfoService {

}
