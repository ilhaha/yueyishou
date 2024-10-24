package com.ilhaha.yueyishou.model.form.recycler;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author ilhaha
 * @Create 2024/10/24 14:47
 * @Version 1.0
 *
 * 修改回收员评分表单类
 */
@Data
public class UpdateRateForm {


    /**
     * 回收员Id
     */
    private Long recyclerId;

    /**
     * 评分
     */
    private BigDecimal rate;

}
