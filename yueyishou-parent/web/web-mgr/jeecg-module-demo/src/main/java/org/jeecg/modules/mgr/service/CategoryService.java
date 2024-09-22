package org.jeecg.modules.mgr.service;

import com.ilhaha.yueyishou.model.entity.category.CategoryInfo;
import com.ilhaha.yueyishou.model.form.category.UpdateCategoryStatusForm;


import java.util.List;

/**
 * @Author ilhaha
 * @Create 2024/9/6 17:07
 * @Version 1.0
 */
public interface CategoryService {
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
     * 添加废品品类信息
     * @param categoryInfo
     */
    void add(CategoryInfo categoryInfo);

    /**
     * 修改废品品类信息
     * @param categoryInfo
     */
    void edit(CategoryInfo categoryInfo);

    /**
     * 根据id删除废品品类
     * @param id
     * @return
     */
    String delete(String id);

    /**
     * 根据id批量删除废品品类
     * @param ids
     * @return
     */
    String deleteBatch(String ids);

    /**
     * 根据id查询废品品类信息
     * @param id
     * @return
     */
    CategoryInfo queryById(String id);

    /**
     * 切换废品品类状态
     * @return
     */
    String switchStatus(UpdateCategoryStatusForm updateCategoryStatusForm);
}
