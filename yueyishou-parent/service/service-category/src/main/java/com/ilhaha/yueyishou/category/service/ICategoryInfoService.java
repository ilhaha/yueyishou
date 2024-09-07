package com.ilhaha.yueyishou.category.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ilhaha.yueyishou.entity.category.CategoryInfo;
import com.ilhaha.yueyishou.form.category.UpdateCategoryStatusForm;

import java.util.List;

public interface ICategoryInfoService extends IService<CategoryInfo> {

    /**
     * 废品回收品类分层查询
     * @return
     */
    List<CategoryInfo> queryPageList();

    /**
     * 获取所有的父废品品类
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

}
