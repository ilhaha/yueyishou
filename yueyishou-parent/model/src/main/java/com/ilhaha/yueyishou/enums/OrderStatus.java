package com.ilhaha.yueyishou.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

/**
 * @Author ilhaha
 * @Create 2024/9/2 22:00
 * @Version 1.0
 *
 * 订单状态
 */
@Getter
public enum OrderStatus {

    /**订单状态：
     * 1等待接单，
     * 2回收员已接单，
     * 3回收员已到达，
     * 4顾客确定订单，
     * 5回收员未付款，
     * 6回收员已付款，
     * 7订单已完成，
     * 8订单已取消*/

    WAITING_ACCEPT(1, "等待接单"),
    RECYCLER_ACCEPTED(2, "回收员已接单"),
    RECYCLER_ARRIVED(3, "回收员已到达"),
    CUSTOMER_CONFIRM_ORDER(4, "顾客确定订单"),
    RECYCLER_UNPAID(5,"回收员未付款"),
    RECYCLER_PAID(6,"回收员已付款"),
    COMPLETED_ORDER(7,"订单已完成"),
    CANCELED_ORDER(8,"订单已取消"),;


    @EnumValue
    private Integer status;
    private String comment;

    OrderStatus(Integer status, String comment) {
        this.status = status;
        this.comment = comment;
    }


}
