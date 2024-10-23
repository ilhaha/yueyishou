package com.ilhaha.yueyishou.model.form.order;

import lombok.Data;

/**
 * @Author ilhaha
 * @Create 2024/10/23 15:24
 * @Version 1.0
 * <p>
 * 结算订单表单类
 */
@Data
public class SettlementForm {

    /**
     * 回收员Id
     */
    private Long recyclerId;

    /**
     * 订单Id
     */
    private Long orderId;
}
