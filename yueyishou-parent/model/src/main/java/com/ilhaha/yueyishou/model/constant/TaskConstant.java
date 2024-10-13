package com.ilhaha.yueyishou.model.constant;

/**
 * @Author ilhaha
 * @Create 2024/10/13 11:36
 * @Version 1.0
 */
public class TaskConstant {

    /**
     * 处理预约超时订单的定时任务名
     */
    public static final String PROCESS_TIMEOUT_ORDERS_TASK = "processTimeoutOrdersTask";

    /**
     * 处理预约超时订单的定时任务参数
     */
    public static final String PROCESS_TIMEOUT_ORDERS_TASK_PARAMS = "";

    /**
     * 每个多少时间执行处理预约超时订单的定时任务
     */
//    public static final String PROCESS_TIMEOUT_ORDERS_TASK_CORN = "*/10 * * * * ?";
    public static final String PROCESS_TIMEOUT_ORDERS_TASK_CORN = "0 0/1 * * * ?";

    /**
     * 处理预约超时订单的定时任务描述
     */
    public static final String PROCESS_TIMEOUT_ORDERS_TASK_DESC = "取消回收预约时间超时的订单";

    /**
     * 任务执行成功状态
     */
    public static final Integer TASK_EXEC_SUCCESS_STATUS = 1;

    /**
     * 任务执行失败状态
     */
    public static final Integer TASK_EXEC_ERROR_STATUS = 0;


}
