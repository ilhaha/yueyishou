package com.ilhaha.yueyishou.customer.controller;

import com.ilhaha.yueyishou.common.anno.LoginVerification;
import com.ilhaha.yueyishou.model.entity.category.CategoryInfo;
import com.ilhaha.yueyishou.common.result.Result;
import com.ilhaha.yueyishou.customer.service.CategoryService;
import com.ilhaha.yueyishou.model.vo.category.SubCategoryVo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author ilhaha
 * @Create 2024/9/21 16:17
 * @Version 1.0
 */
@RestController
@RequestMapping("/category")
public class CategoryController {

    @Resource
    private CategoryService categoryService;

    /**
     * 获取父品类的所有子品类
     * @param parentId
     * @return
     */
    @LoginVerification
    @GetMapping("/sub/{parentId}")
    public Result<List<SubCategoryVo>> getSubCategories(@PathVariable("parentId") Long parentId){
        return categoryService.getSubCategories(parentId);
    }

    /**
     * 获取已启用的废品品类树
     * @return
     */
    @LoginVerification
    @GetMapping("/tree")
    public Result<List<CategoryInfo>> getCategoryTree(){
        return categoryService.getCategoryTree();
    }

    /**
     * 获取所有已启用的父废品品类
     * @return
     */
    @LoginVerification
    @GetMapping("/parent/list")
    public Result<List<CategoryInfo>> parentList(){
        return Result.ok(categoryService.parentList());
    };

}
