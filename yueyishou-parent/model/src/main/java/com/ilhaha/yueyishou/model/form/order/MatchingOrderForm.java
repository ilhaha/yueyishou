package com.ilhaha.yueyishou.model.form.order;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author ilhaha
 * @Create 2024/10/11 20:24
 * @Version 1.0
 *
 * 获取符合回收员接单的表单类
 */
@Data
public class MatchingOrderForm {

    /**
     * 当前回收员对应的用户Id
     */
    private Long customerId;

    /**
     * 回收员Id
     */
    private Long recyclerId;

    /**
     * 回收员接单里程
     */
    private BigDecimal acceptDistance;

    /**
     * 回收员回收的类型
     */
    private String recyclingType;

}
