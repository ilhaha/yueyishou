package com.ilhaha.yueyishou.model.enums;

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

    WAITING_ACCEPT(1, "等待接单"),
    RECYCLER_ACCEPTED(2, "回收员已接单"),
    GOTO_RECYCLING_POINT(3, "回收员前往回收点"),
    CUSTOMER_CONFIRM_ORDER(4, "顾客确定订单"),
    RECYCLER_UNPAID(5,"回收员未付款"),
    COMPLETED_ORDER(6,"回收员已付款订单完成"),
    AWAITING_EVALUATION(7,"待评价"),
    CANCELED_ORDER(8,"订单已取消"),;


    @EnumValue
    private Integer status;
    private String comment;

    OrderStatus(Integer status, String comment) {
        this.status = status;
        this.comment = comment;
    }


}
