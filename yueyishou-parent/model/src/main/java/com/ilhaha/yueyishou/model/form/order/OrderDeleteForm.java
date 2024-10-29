package com.ilhaha.yueyishou.model.form.order;

import lombok.Data;

/**
 * @Author ilhaha
 * @Create 2024/10/28 17:35
 * @Version 1.0
 *
 * 删除订单表单类
 */
@Data
public class OrderDeleteForm {
    /**
     * 顾客ID
     */
    private Long customerId;

    /**
     * 回收员ID
     */
    private Long recyclerId;
    /**
     * 订单ID
     */
    private Long orderId;
}
