package com.ilhaha.yueyishou.dispatch.service;

/**
 * @Author ilhaha
 * @Create 2024/10/13 11:31
 * @Version 1.0
 */
public interface OrderService {
    /**
     * 创建定时取消到预约时间还没接单的订单任务
     * @return
     */
    Long processTimeoutOrdersTask();

    /**
     * 开始执行任务调度
     * @param jobId
     */
    void executeTask(long jobId);
}
