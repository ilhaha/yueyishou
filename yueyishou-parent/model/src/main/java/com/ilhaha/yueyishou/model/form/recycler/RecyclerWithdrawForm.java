package com.ilhaha.yueyishou.model.form.recycler;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author ilhaha
 * @Create 2024/10/22 15:51
 * @Version 1.0
 *
 * 回收员支出/收入表单表单类
 */
@Data
public class RecyclerWithdrawForm {

    /**回收员Id*/
    private Long recyclerId;

    /**支出/收入金额*/
    private BigDecimal amount;
}
