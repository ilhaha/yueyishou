package com.ilhaha.yueyishou.category.client;

import com.ilhaha.yueyishou.model.entity.category.CategoryInfo;
import com.ilhaha.yueyishou.model.form.category.UpdateCategoryStatusForm;
import com.ilhaha.yueyishou.common.result.Result;
import com.ilhaha.yueyishou.model.vo.category.SubCategoryVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author ilhaha
 * @Create 2024/9/2 16:04
 * @Version 1.0
 */
@FeignClient("service-category")
public interface CategoryInfoFeignClient {

    /**
     * 获取父品类的所有子品类
     * @param parentId
     * @return
     */
    @GetMapping("/categoryInfo/sub/{parentId}")
    Result<List<SubCategoryVo>> getSubCategories(@PathVariable("parentId") Long parentId);

    /**
     * 获取已启用的废品品类树
     * @return
     */
    @GetMapping("/categoryInfo/tree")
    Result<List<CategoryInfo>> getCategoryTree();

    /**
     * 切换废品品类状态
     * @return
     */
    @PostMapping("/categoryInfo/switch/status")
    Result<String> switchStatus(@RequestBody UpdateCategoryStatusForm updateCategoryStatusForm);

    /**
     * 获取所有已启动的父废品品类
     * @return
     */
    @GetMapping("/categoryInfo/parent/list")
    Result<List<CategoryInfo>> parentList();

    /**
     * 列表查询
     * @return
     */
    @GetMapping(value = "/categoryInfo/list")
    Result<List<CategoryInfo>> queryPageList();

    /**
     *   添加
     *
     * @param categoryInfo
     * @return
     */
    @PostMapping(value = "/categoryInfo/add")
    Result<Boolean> add(@RequestBody CategoryInfo categoryInfo);

    /**
     *  编辑
     *
     * @param categoryInfo
     * @return
     */
    @PutMapping(value = "/categoryInfo/edit")
    Result<Boolean> edit(@RequestBody CategoryInfo categoryInfo);

    /**
     *   通过id删除
     *
     * @param id
     * @return
     */
    @DeleteMapping(value = "/categoryInfo/delete")
    Result<Boolean> delete(@RequestParam(name="id",required=true) String id);

    /**
     *  批量删除
     *
     * @param ids
     * @return
     */
    @DeleteMapping(value = "/categoryInfo/deleteBatch")
    Result<Boolean> deleteBatch(@RequestParam(name="ids",required=true) String ids);

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/categoryInfo/queryById")
    Result<CategoryInfo> queryById(@RequestParam(name="id",required=true) String id);
}
