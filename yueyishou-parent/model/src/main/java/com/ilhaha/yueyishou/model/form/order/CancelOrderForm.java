package com.ilhaha.yueyishou.model.form.order;

import lombok.Data;

import java.util.Date;

/**
 * @Author ilhaha
 * @Create 2024/10/24 21:21
 * @Version 1.0
 *
 * 接单后取消订单表单类
 */
@Data
public class CancelOrderForm {

    /**订单Id*/
    private Long orderId;

    /**顾客Id*/
    private Long customerId;

    /**回收员Id*/
    private Long recyclerId;

    /**预约时间*/
    private Date appointmentTime;

    /**回收员接单时间*/
    private Date acceptTime;

    /**取消订单操作人*/
    private String cancelOperator;
}
