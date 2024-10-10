package com.ilhaha.yueyishou.model.form.recycler;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author ilhaha
 * @Create 2024/10/10 23:05
 * @Version 1.0
 *
 * 回收员修改接单设置表单类
 */
@Data
public class OrderSettingForm {

    /**回收员ID*/
    private Long recyclerId;
    /**回收类型，多个使用逗号隔开*/
    private String recyclingType;
    /**接单里程设置*/
    private BigDecimal acceptDistance;

}
