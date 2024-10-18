package com.ilhaha.yueyishou.recycler.service.impl;

import com.ilhaha.yueyishou.category.client.CategoryInfoFeignClient;
import com.ilhaha.yueyishou.common.result.Result;
import com.ilhaha.yueyishou.model.entity.category.CategoryInfo;
import com.ilhaha.yueyishou.recycler.service.CategoryService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author ilhaha
 * @Create 2024/10/10 20:54
 * @Version 1.0
 */
@Service
public class CategoryServiceImpl implements CategoryService {

    @Resource
    private CategoryInfoFeignClient categoryInfoFeignClient;

    /**
     * 获取所有已启用的父废品品类
     * @return
     */
    @Override
    public Result<List<CategoryInfo>> parentList() {
        return categoryInfoFeignClient.parentList();
    }

    /**
     * 获取已启用的废品品类树
     * @return
     */
    @Override
    public Result<List<CategoryInfo>> getCategoryTree() {
        return categoryInfoFeignClient.getCategoryTree();
    }
}
