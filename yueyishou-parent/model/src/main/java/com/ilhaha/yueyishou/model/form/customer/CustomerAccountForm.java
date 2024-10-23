package com.ilhaha.yueyishou.model.form.customer;

import lombok.Data;

/**
 * @Author ilhaha
 * @Create 2024/10/23 23:06
 * @Version 1.0
 *
 * 查询顾客账户信息表单类
 */
@Data
public class CustomerAccountForm {

    /**顾客Id*/
    private Long customerId;

    /**明细日期*/
    private String detailsTime;
}
