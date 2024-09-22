package com.ilhaha.yueyishou.category.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ilhaha.yueyishou.model.entity.category.CategoryInfo;
import com.ilhaha.yueyishou.model.form.category.UpdateCategoryStatusForm;

import java.util.List;

public interface ICategoryInfoService extends IService<CategoryInfo> {

    /**
     * 废品回收品类分层查询
     * @return
     */
    List<CategoryInfo> queryPageList();

    /**
     * 获取所有已启用的父废品品类
     * @return
     */
    List<CategoryInfo> parentList();

    /**
     * 根据品类名称查询品类信息
     * @param categoryName
     * @return
     */
    CategoryInfo getCategoryByName(String categoryName);

    /**
     * 切换废品品类状态
     * @return
     */
    String switchStatus(UpdateCategoryStatusForm updateCategoryStatusForm);

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    CategoryInfo queryById(String id);

    /**
     * 获取已启用的废品品类树
     * @return
     */
    List<CategoryInfo> getCategoryTree();


}
