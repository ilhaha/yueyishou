package com.ilhaha.yueyishou.model.form.customer;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author ilhaha
 * @Create 2024/10/23 22:15
 * @Version 1.0
 *
 * 顾客支出/收入表单类
 */
@Data
public class CustomerWithdrawForm {


    /**顾客Id*/
    private Long customerId;

    /**支出/收入金额*/
    private BigDecimal amount;
}
