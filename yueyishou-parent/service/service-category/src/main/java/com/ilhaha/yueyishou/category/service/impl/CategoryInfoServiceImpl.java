package com.ilhaha.yueyishou.category.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ilhaha.yueyishou.model.constant.CategoryConstant;
import com.ilhaha.yueyishou.model.entity.category.CategoryInfo;
import com.ilhaha.yueyishou.category.mapper.CategoryInfoMapper;
import com.ilhaha.yueyishou.category.service.ICategoryInfoService;
import com.ilhaha.yueyishou.model.form.category.UpdateCategoryStatusForm;
import com.ilhaha.yueyishou.tencentcloud.client.CosFeignClient;
import jakarta.annotation.Resource;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryInfoServiceImpl extends ServiceImpl<CategoryInfoMapper, CategoryInfo> implements ICategoryInfoService {

    @Resource
    private CosFeignClient cosFeignClient;

    /**
     * 废品回收品类分层查询
     *
     * @return
     */
    @Cacheable(value = "category", key = "T(com.ilhaha.yueyishou.common.constant.RedisConstant).CATEGORY_TREE")
    @Override
    public List<CategoryInfo> queryPageList() {
        // 查处所有的废品品类父级
        LambdaQueryWrapper<CategoryInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CategoryInfo::getParentId, CategoryConstant.FIRST_LEVEL_CATEGORY_ID);
        List<CategoryInfo> categoryInfos = this.list(wrapper);
        // 根据父级查询所有的子品类
        List<CategoryInfo> result = categoryInfos.stream().map(item -> {
            item.setIcon(ObjectUtils.isEmpty(item.getIcon()) ? item.getIcon() : cosFeignClient.getImageUrl(item.getIcon()).getData());
            LambdaQueryWrapper<CategoryInfo> categoryInfoLambdaQueryWrapper = new LambdaQueryWrapper<>();
            categoryInfoLambdaQueryWrapper.eq(CategoryInfo::getParentId, item.getId());
            List<CategoryInfo> child = this.list(categoryInfoLambdaQueryWrapper);
            for (CategoryInfo categoryInfo : child) {
                categoryInfo.setIcon(ObjectUtils.isEmpty(categoryInfo.getIcon()) ? categoryInfo.getIcon() : cosFeignClient.getImageUrl(categoryInfo.getIcon()).getData());
            }
            item.setChildren(child);
            return item;
        }).collect(Collectors.toList());


        return result;
    }

    /**
     * 获取所有已启用的父废品品类
     *
     * @return
     */
    @Override
    public List<CategoryInfo> parentList() {
        LambdaQueryWrapper<CategoryInfo> categoryInfoLambdaQueryWrapper = new LambdaQueryWrapper<>();
        categoryInfoLambdaQueryWrapper.eq(CategoryInfo::getParentId, CategoryConstant.FIRST_LEVEL_CATEGORY_ID)
                .eq(CategoryInfo::getStatus, CategoryConstant.ENABLE_STATUS);
        List<CategoryInfo> list = this.list(categoryInfoLambdaQueryWrapper);
        return list.stream().map(item -> {
            item.setIcon(ObjectUtils.isEmpty(item.getIcon()) ? item.getIcon() : cosFeignClient.getImageUrl(item.getIcon()).getData());
            return item;
        }).collect(Collectors.toList());
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
    @Caching(evict = {
            @CacheEvict(value = "category",
                    key = "T(com.ilhaha.yueyishou.common.constant.RedisConstant).CATEGORY_TREE"),
            @CacheEvict(value = "userCategory",
                    key = "T(com.ilhaha.yueyishou.common.constant.RedisConstant).CATEGORY_TREE")
    })
    @Override
    public String switchStatus(UpdateCategoryStatusForm updateCategoryStatusForm) {
        LambdaUpdateWrapper<CategoryInfo> categoryInfoLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        categoryInfoLambdaUpdateWrapper.set(CategoryInfo::getStatus, updateCategoryStatusForm.getStatus())
                .in(CategoryInfo::getId, updateCategoryStatusForm.getCategoryIds());
        this.update(categoryInfoLambdaUpdateWrapper);
        return "修改成功";
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @Override
    public CategoryInfo queryById(String id) {
        CategoryInfo categoryInfo = this.getById(id);
        if (!ObjectUtils.isEmpty(categoryInfo)) {
            categoryInfo.setIcon(ObjectUtils.isEmpty(categoryInfo.getIcon()) ? categoryInfo.getIcon() : cosFeignClient.getImageUrl(categoryInfo.getIcon()).getData());
        }
        return categoryInfo;
    }

    /**
     * 获取已启用的废品品类树
     * @return
     */
    @Cacheable(value = "userCategory", key = "T(com.ilhaha.yueyishou.common.constant.RedisConstant).CATEGORY_TREE")
    @Override
    public List<CategoryInfo> getCategoryTree() {
        // 查处所有的废品品类父级
        LambdaQueryWrapper<CategoryInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CategoryInfo::getParentId, CategoryConstant.FIRST_LEVEL_CATEGORY_ID)
                .eq(CategoryInfo::getStatus,CategoryConstant.ENABLE_STATUS);
        List<CategoryInfo> categoryInfos = this.list(wrapper);
        // 根据父级查询所有的子品类
        List<CategoryInfo> result = categoryInfos.stream().map(item -> {
            item.setIcon(ObjectUtils.isEmpty(item.getIcon()) ? item.getIcon() : cosFeignClient.getImageUrl(item.getIcon()).getData());
            LambdaQueryWrapper<CategoryInfo> categoryInfoLambdaQueryWrapper = new LambdaQueryWrapper<>();
            categoryInfoLambdaQueryWrapper.eq(CategoryInfo::getParentId, item.getId())
                    .eq(CategoryInfo::getStatus,CategoryConstant.ENABLE_STATUS);
            List<CategoryInfo> child = this.list(categoryInfoLambdaQueryWrapper);
            for (CategoryInfo categoryInfo : child) {
                categoryInfo.setIcon(ObjectUtils.isEmpty(categoryInfo.getIcon()) ? categoryInfo.getIcon() : cosFeignClient.getImageUrl(categoryInfo.getIcon()).getData());
            }
            item.setChildren(child);
            return item;
        }).collect(Collectors.toList());


        return result;
    }
}
