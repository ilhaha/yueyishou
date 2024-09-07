package com.ilhaha.yueyishou.category.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ilhaha.yueyishou.constant.CategoryConstant;
import com.ilhaha.yueyishou.constant.RedisConstant;
import com.ilhaha.yueyishou.entity.category.CategoryInfo;
import com.ilhaha.yueyishou.category.mapper.CategoryInfoMapper;
import com.ilhaha.yueyishou.category.service.ICategoryInfoService;
import com.ilhaha.yueyishou.form.category.UpdateCategoryStatusForm;
import jakarta.annotation.Resource;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryInfoServiceImpl extends ServiceImpl<CategoryInfoMapper, CategoryInfo> implements ICategoryInfoService {
    /**
     * 废品回收品类分层查询
     *
     * @return
     */
    @Cacheable(value = "category", key = "T(com.ilhaha.yueyishou.constant.RedisConstant).CATEGORY_TREE")
    @Override
    public List<CategoryInfo> queryPageList() {
        // 查处所有的废品品类父级
        LambdaQueryWrapper<CategoryInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CategoryInfo::getParentId, CategoryConstant.FIRST_LEVEL_CATEGORY_ID);
        List<CategoryInfo> categoryInfos = this.list(wrapper);
        // 根据父级查询所有的子品类
        List<CategoryInfo> result = categoryInfos.stream().map(item -> {
            LambdaQueryWrapper<CategoryInfo> categoryInfoLambdaQueryWrapper = new LambdaQueryWrapper<>();
            categoryInfoLambdaQueryWrapper.eq(CategoryInfo::getParentId, item.getId());
            List<CategoryInfo> child = this.list(categoryInfoLambdaQueryWrapper);
            item.setChildren(child);
            return item;
        }).collect(Collectors.toList());


        return result;
    }

    /**
     * 获取所有的父废品品类
     *
     * @return
     */
    @Override
    public List<CategoryInfo> parentList() {
        LambdaQueryWrapper<CategoryInfo> categoryInfoLambdaQueryWrapper = new LambdaQueryWrapper<>();
        categoryInfoLambdaQueryWrapper.eq(CategoryInfo::getParentId, CategoryConstant.FIRST_LEVEL_CATEGORY_ID)
                .eq(CategoryInfo::getStatus, CategoryConstant.ENABLE_STATUS);
        return this.list(categoryInfoLambdaQueryWrapper);
    }

    /**
     * 根据品类名称查询品类信息
     *
     * @param categoryName
     * @return
     */
    @Override
    public CategoryInfo getCategoryByName(String categoryName) {
        LambdaQueryWrapper<CategoryInfo> categoryInfoLambdaQueryWrapper = new LambdaQueryWrapper<>();
        categoryInfoLambdaQueryWrapper.eq(CategoryInfo::getCategoryName, categoryName);
        return this.getOne(categoryInfoLambdaQueryWrapper);
    }

    /**
     * 修改废品品类状态
     *
     * @param updateCategoryStatusForm
     * @return
     */
    @CacheEvict(value = "category",
            key = "T(com.ilhaha.yueyishou.constant.RedisConstant).CATEGORY_TREE")
    @Override
    public String switchStatus(UpdateCategoryStatusForm updateCategoryStatusForm) {
        LambdaUpdateWrapper<CategoryInfo> categoryInfoLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        categoryInfoLambdaUpdateWrapper.set(CategoryInfo::getStatus, updateCategoryStatusForm.getStatus())
                .eq(CategoryInfo::getId, updateCategoryStatusForm.getCategoryId());
        this.update(categoryInfoLambdaUpdateWrapper);
        return "修改成功";
    }
}
