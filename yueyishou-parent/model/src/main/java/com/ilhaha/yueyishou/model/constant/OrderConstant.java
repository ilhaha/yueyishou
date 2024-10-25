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

    /**
     * 回收员到达时间与预约时间相差多少分钟不算超时
     * */
    public static final Integer NOT_TIMED_OUT_MIN = 0;

    /**
     * 取消订单操作人：顾客
     */
    public static final String CUSTOMER_CANCELS_ORDER = "customer";

    /**
     * 取消订单操作人：回收员
     */
    public static final String RECYCLER_CANCELS_ORDER = "recycler";

    /**
     * 顾客免费取消订单的时间限制（当前时间距离回收员接单时间不超过五分钟）
     */
    public static final long CUSTOMER_CANCELS_ORDER_FREE_MIN = 5;

    /**
     * 回收员免费取消订单的时间限制（当前时间距离预约时间时间不低于60分钟）
     */
    public static final long RECYCLER_CANCELS_ORDER_FREE_MIN = 60;

}
