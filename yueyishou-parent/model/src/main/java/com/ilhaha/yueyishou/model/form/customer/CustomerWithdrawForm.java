package com.ilhaha.yueyishou.model.form.customer;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author ilhaha
 * @Create 2024/10/23 22:15
 * @Version 1.0
 *
 * 顾客结算、提现表单类
 */
@Data
public class CustomerWithdrawForm {


    /**顾客Id*/
    private Long customerId;

    /**结算订单结算、提现金额*/
    private BigDecimal amount;
}
