package com.ilhaha.yueyishou.model.constant;

import lombok.Data;

/**
 * @Author ilhaha
 * @Create 2024/10/9 22:14
 * @Version 1.0
 * <p>
 * 订单常量
 */
@Data
public class OrderConstant {


    /**
     * 不拒收订单状态
     */
    public static final Integer NO_REJECTION_STATUS = 0;

    /**
     * 拒收订单状态
     */
    public static final Integer DENIED_STATUS = 1;

    /**
     * 拒收申请通过状态
     */
    public static final Integer REJECT_APPLICATION_STATUS = 2;

    /**
     * 拒收申请未通过
     */
    public static final Integer REJECTION_APPLICATION_FAILED = -1;

    /**
     * 顾客取消订单备注
     */
    public static final String CANCEL_REMARK = "用户自行取消";


    /**
     * 预约时间超时取消订单备注
     */
    public static final String TIMEOUT_CANCEL_REMARK = "预约时间超时未接单";

    /**
     * 回收员在预约时间未到，取消订单备注
     */
    public static final String RECYCLER_LATE_CANCELLATION_NOTE = "未在预约时间内服务取消";

    /**
     * 回收员在预约时间未到不足60分钟取消，取消订单备注
     */
    public static final String SHORT_NOTICE_CANCELLATION = "预约前60分钟内取消";

    /**
     * 回收员到达时间与预约时间相差多少分钟不算超时
     */
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
