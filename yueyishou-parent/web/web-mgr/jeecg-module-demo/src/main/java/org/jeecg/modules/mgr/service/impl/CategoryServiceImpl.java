package org.jeecg.modules.mgr.service.impl;

import com.ilhaha.yueyishou.category.client.CategoryInfoFeignClient;
import com.ilhaha.yueyishou.entity.category.CategoryInfo;
import com.ilhaha.yueyishou.form.category.UpdateCategoryStatusForm;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.modules.mgr.service.CategoryService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author ilhaha
 * @Create 2024/9/6 17:07
 * @Version 1.0
 */
@Service
public class CategoryServiceImpl implements CategoryService {

    @Resource
    private CategoryInfoFeignClient categoryInfoFeignClient;

    /**
     * 废品回收品类分层查询
     * @return
     */
    @Override
    public List<CategoryInfo> queryPageList() {
        return categoryInfoFeignClient.queryPageList().getData();
    }

    /**
     * 获取所有的父废品品类
     * @return
     */
    @Override
    public List<CategoryInfo> parentList() {
        return categoryInfoFeignClient.parentList().getData();
    }


    /**
     * 添加废品品类信息
     * @param categoryInfo
     */
    @Override
    public void add(CategoryInfo categoryInfo) {
        Boolean flag = categoryInfoFeignClient.add(categoryInfo).getData();
        if (!flag) {
            throw new JeecgBootException("废品品类名称已存在！");
        }
    }

    /**
     * 修改废品品类信息
     * @param categoryInfo
     */
    @Override
    public void edit(CategoryInfo categoryInfo) {
        Boolean flag = categoryInfoFeignClient.edit(categoryInfo).getData();
        if (!flag) {
            throw new JeecgBootException("废品品类名称已存在！");
        }
    }

    /**
     * 根据id删除废品品类
     * @param id
     * @return
     */
    @Override
    public String delete(String id) {
        Boolean flag = categoryInfoFeignClient.delete(id).getData();
        if (!flag) {
            throw new JeecgBootException("具有子品类的品类不能删除");
        }
        return "删除成功";
    }


    /**
     * 根据id批量删除废品品类
     * @param ids
     * @return
     */
    @Override
    public String deleteBatch(String ids) {
        Boolean flag = categoryInfoFeignClient.deleteBatch(ids).getData();
        if (!flag) {
            throw new JeecgBootException("具有子品类的品类不能删除");
        }
        return "删除成功";
    }

    /**
     * 根据id查询废品品类信息
     * @param id
     * @return
     */
    @Override
    public CategoryInfo queryById(String id) {
        return categoryInfoFeignClient.queryById(id).getData();
    }

    /**
     * 切换废品品类状态
     * @return
     */
    @Override
    public String switchStatus(UpdateCategoryStatusForm updateCategoryStatusForm) {
        return categoryInfoFeignClient.switchStatus(updateCategoryStatusForm).getData();
    }
}
