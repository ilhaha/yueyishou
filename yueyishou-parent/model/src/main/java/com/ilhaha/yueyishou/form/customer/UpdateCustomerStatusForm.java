package com.ilhaha.yueyishou.form.customer;

import lombok.Data;

import java.util.List;

/**
 * @Author ilhaha
 * @Create 2024/9/7 23:35
 * @Version 1.0
 */
@Data
public class UpdateCustomerStatusForm {

    /**客户ID集合*/
    private List<Long> customerIds;

    /**状态*/
    private Integer status;
}
