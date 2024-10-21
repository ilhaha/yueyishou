package com.ilhaha.yueyishou.model.form.order;

import lombok.Data;

/**
 * @Author ilhaha
 * @Create 2024/10/20 10:58
 * @Version 1.0
 *
 * 校验回收码表单类
 */
@Data
public class ValidateRecycleCodeForm {

    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 订单回收码
     */
    private String recycleCode;
}
