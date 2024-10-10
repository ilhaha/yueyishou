package com.ilhaha.yueyishou.recycler.service;

import com.ilhaha.yueyishou.common.result.Result;
import com.ilhaha.yueyishou.model.entity.category.CategoryInfo;

import java.util.List;

/**
 * @Author ilhaha
 * @Create 2024/10/10 20:54
 * @Version 1.0
 */
public interface CategoryService {

    /**
     * 获取所有已启用的父废品品类
     * @return
     */
    Result<List<CategoryInfo>> parentList();

}
