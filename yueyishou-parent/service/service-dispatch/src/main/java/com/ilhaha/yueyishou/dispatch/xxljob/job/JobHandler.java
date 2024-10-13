package com.ilhaha.yueyishou.dispatch.xxljob.job;


import com.alibaba.nacos.common.utils.ExceptionUtil;
import com.ilhaha.yueyishou.dispatch.mapper.XxlJobLogMapper;
import com.ilhaha.yueyishou.dispatch.service.OrderService;
import com.ilhaha.yueyishou.model.constant.TaskConstant;
import com.ilhaha.yueyishou.model.entity.dispatch.XxlJobLog;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JobHandler {

    @Resource
    private XxlJobLogMapper xxlJobLogMapper;

    @Resource
    private OrderService orderService;

    @XxlJob(TaskConstant.PROCESS_TIMEOUT_ORDERS_TASK)
    public void processTimeoutOrders(){
        log.info(TaskConstant.PROCESS_TIMEOUT_ORDERS_TASK_DESC + "：{}", XxlJobHelper.getJobId());

        //记录定时任务相关的日志信息
        //封装日志对象
        XxlJobLog xxlJobLog = new XxlJobLog();
        xxlJobLog.setJobId(XxlJobHelper.getJobId());
        long startTime = System.currentTimeMillis();
        try {
            //执行任务
            orderService.executeTask(XxlJobHelper.getJobId());
            // 执行成功
            xxlJobLog.setStatus(TaskConstant.TASK_EXEC_SUCCESS_STATUS);
        } catch (Exception e) {
            // 执行失败
            xxlJobLog.setStatus(TaskConstant.TASK_EXEC_ERROR_STATUS);
            xxlJobLog.setError(ExceptionUtil.getAllExceptionMsg(e));
            log.error("定时任务执行失败，任务id为：{}", XxlJobHelper.getJobId());
            e.printStackTrace();
        } finally {
            //耗时
            Long times = System.currentTimeMillis() - startTime;
            xxlJobLog.setTimes(times);
            xxlJobLogMapper.insert(xxlJobLog);
        }
    }
}