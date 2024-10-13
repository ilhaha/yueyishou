package com.ilhaha.yueyishou.model.constant;

import lombok.Data;

/**
 * @Author ilhaha
 * @Create 2024/10/9 22:14
 * @Version 1.0
 *
 * 订单常量
 */
@Data
public class OrderConstant {

    /**
     * 顾客取消订单备注
     */
    public static final String CANCEL_REMARK = "用户自行取消";

    /**
     * 预约时间超时取消订单备注
     */
    public static final String TIMEOUT_CANCEL_REMARK = "预约时间超时未接单";
}
