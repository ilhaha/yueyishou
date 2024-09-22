package org.jeecg.modules.mgr.controller;

import com.ilhaha.yueyishou.model.entity.category.CategoryInfo;
import com.ilhaha.yueyishou.model.form.category.UpdateCategoryStatusForm;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.mgr.service.CategoryService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author ilhaha
 * @Create 2024/9/6 17:06
 * @Version 1.0
 */
@RestController
@RequestMapping("/category")
public class CategoryController {

    @Resource
    private CategoryService categoryService;

    /**
     * 切换废品品类状态
     * @return
     */
    @PostMapping("/switch/status")
    public Result<String> switchStatus(@RequestBody UpdateCategoryStatusForm updateCategoryStatusForm){
        return Result.OK(categoryService.switchStatus(updateCategoryStatusForm));
    }

    /**
     * 获取所有已启用的父废品品类
     * @return
     */
    @GetMapping("/parent/list")
    public Result<List<CategoryInfo>> parentList(){
       return Result.OK(categoryService.parentList());
    };

    /**
     * 废品回收品类分层查询
     * @return
     */
    @GetMapping(value = "/list")
    Result<List<CategoryInfo>> queryPageList(){
        return Result.OK(categoryService.queryPageList());
    }

    /**
     *   添加
     *
     * @param categoryInfo
     * @return
     */
    @PostMapping(value = "/add")
    Result<String> add(@RequestBody CategoryInfo categoryInfo) {
        categoryService.add(categoryInfo);
        return Result.OK("添加成功");
    }

    /**
     *  编辑
     *
     * @param categoryInfo
     * @return
     */
    @PutMapping(value = "/edit")
    Result<Boolean> edit(@RequestBody CategoryInfo categoryInfo) {
        categoryService.edit(categoryInfo);
        return Result.OK("修改成功");
    }

    /**
     *   通过id删除
     *
     * @param id
     * @return
     */
    @DeleteMapping(value = "/delete")
    Result<String> delete(@RequestParam(name="id",required=true) String id){
        return Result.OK(categoryService.delete(id));
    }


    /**
     *  批量删除
     *
     * @param ids
     * @return
     */
    @DeleteMapping(value = "/deleteBatch")
    Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
        return Result.OK(categoryService.deleteBatch(ids));
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/queryById")
    Result<CategoryInfo> queryById(@RequestParam(name="id",required=true) String id) {
       return Result.OK(categoryService.queryById(id));
    }
}
