package com.ilhaha.yueyishou.form.category;

import lombok.Data;

import java.util.List;

/**
 * @Author ilhaha
 * @Create 2024/9/7 14:04
 * @Version 1.0
 */
@Data
public class UpdateCategoryStatusForm {

    /**品类id集合*/
    private List<Long> categoryIds;

    /**状态*/
    private Integer status;
}
