package com.ilhaha.yueyishou.model.form.recycler;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author ilhaha
 * @Create 2024/10/22 16:09
 * @Version 1.0
 *
 * 增加账户明细表单类
 */
@Data
public class AddDetailsForm {

    /**账户id*/
    private Long accountId;

    /**交易金额*/
    private BigDecimal amount;

    /**交易类型：1回收支付费用 2提现 3充值*/
    private String tradeType;

    /**交易内容*/
    private String content;
}
