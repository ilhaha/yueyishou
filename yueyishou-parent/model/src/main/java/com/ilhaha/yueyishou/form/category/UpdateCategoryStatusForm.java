package com.ilhaha.yueyishou.form.category;

import lombok.Data;

/**
 * @Author ilhaha
 * @Create 2024/9/7 14:04
 * @Version 1.0
 */
@Data
public class UpdateCategoryStatusForm {

    /**品类id*/
    private Long categoryId;

    /**状态*/
    private Integer status;
}
