package com.ilhaha.yueyishou.dispatch.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ilhaha.yueyishou.dispatch.mapper.XxlJobMapper;
import com.ilhaha.yueyishou.dispatch.service.OrderService;
import com.ilhaha.yueyishou.dispatch.xxljob.client.XxlJobClient;
import com.ilhaha.yueyishou.model.constant.RedisConstant;
import com.ilhaha.yueyishou.model.constant.TaskConstant;
import com.ilhaha.yueyishou.model.entity.dispatch.XXLJob;
import com.ilhaha.yueyishou.model.entity.order.OrderInfo;
import com.ilhaha.yueyishou.order.client.OrderInfoFeignClient;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Author ilhaha
 * @Create 2024/10/13 11:31
 * @Version 1.0
 */
@Service
public class OrderServiceImpl implements OrderService {

    @Resource
    private XxlJobMapper xxlJobMapper;

    @Resource
    private XxlJobClient xxlJobClient;

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private OrderInfoFeignClient orderInfoFeignClient;

    /**
     * 创建定时取消到预约时间还没接单的订单任务
     *
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Long processTimeoutOrdersTask() {
        XXLJob xxlJob = xxlJobMapper.selectOne(new LambdaQueryWrapper<XXLJob>().eq(XXLJob::getTaskName, TaskConstant.PROCESS_TIMEOUT_ORDERS_TASK));
        if (ObjectUtils.isEmpty(xxlJob)) {
            Long jobId = xxlJobClient.addAndStart(TaskConstant.PROCESS_TIMEOUT_ORDERS_TASK,
                    TaskConstant.PROCESS_TIMEOUT_ORDERS_TASK_PARAMS, TaskConstant.PROCESS_TIMEOUT_ORDERS_TASK_CORN,
                    TaskConstant.PROCESS_TIMEOUT_ORDERS_TASK_DESC);

            //记录订单与任务的关联信息
            xxlJob = new XXLJob();
            xxlJob.setJobId(jobId);
            xxlJob.setTaskName(TaskConstant.PROCESS_TIMEOUT_ORDERS_TASK);
            xxlJobMapper.insert(xxlJob);
        }
        return xxlJob.getJobId();
    }

    /**
     * 开始进行任务调度
     *
     * @param jobId
     */
    @Override
    public void executeTask(long jobId) {
        // 判断任务是否存在
        LambdaQueryWrapper<XXLJob> xxlJobLambdaQueryWrapper = new LambdaQueryWrapper<>();
        xxlJobLambdaQueryWrapper.eq(XXLJob::getJobId, jobId);
        XXLJob xxlJobDB = xxlJobMapper.selectOne(xxlJobLambdaQueryWrapper);
        // 任务不存在，就不往下执行了
        if (ObjectUtils.isEmpty(xxlJobDB)) {
            return;
        }
        // 查询redis所有的待接单的订单信息
        Set keys = redisTemplate.keys(RedisConstant.SELECT_WAITING_ORDER);

        // 使用 multiGet 批量获取这些键对应的 OrderInfo 对象
        List<OrderInfo> waitOrderInfoList = redisTemplate.opsForValue().multiGet(keys);

        // 筛选出预约时间已超时的订单
        List<OrderInfo> timeoutOrders = this.filterExpiredOrdersByAppointmentTime(waitOrderInfoList);
        if (!ObjectUtils.isEmpty(timeoutOrders)) {
            // 批量删除 Redis 中的记录，并提取超时订单的ID
            List<Long> timeoutOrderIds = timeoutOrders.stream()
                    .peek(order -> redisTemplate.delete(RedisConstant.WAITING_ORDER + order.getId())) // 批量删除Redis键
                    .map(OrderInfo::getId)
                    .collect(Collectors.toList());

            // 如果有超时订单，修改数据库中的数据
            orderInfoFeignClient.processTimeoutOrders(timeoutOrderIds);
        }
    }

    /**
     * 过滤出已过预约时间的订单
     *
     * @param orders 原始订单列表
     * @return 已过预约时间的订单列表
     */
    private List<OrderInfo> filterExpiredOrdersByAppointmentTime(List<OrderInfo> orders) {
        // 获取当前时间
        Date now = new Date();
        return orders.stream()
                // 过滤掉预约时间在当前时间之前的订单
                .filter(order -> order.getAppointmentTime().before(now))
                // 返回过滤后的订单列表
                .collect(Collectors.toList());
    }

}
