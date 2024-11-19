package com.ilhaha.yueyishou.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ilhaha.yueyishou.model.entity.order.OrderRejectionOperate;
import com.ilhaha.yueyishou.model.vo.order.RejectInfoVo;
import com.ilhaha.yueyishou.order.mapper.OrderRejectionOperateMapper;
import com.ilhaha.yueyishou.order.service.IOrderRejectionOperateService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * @Author ilhaha
 * @Create 2024/11/19 11:49
 * @Version 1.0
 */
@Service
public class OrderRejectionOperateServiceImpl extends ServiceImpl<OrderRejectionOperateMapper, OrderRejectionOperate> implements IOrderRejectionOperateService {

    /**
     * 添加审核订单拒收操作记录
     * @param rejectionOperate
     * @return
     */
    @Override
    public Boolean add(OrderRejectionOperate rejectionOperate) {
        return this.save(rejectionOperate);
    }

    /**
     * 获取回收员申请拒收订单被驳回反馈信息
     * @param orderId
     * @return
     */
    @Override
    public RejectInfoVo rejectFeedback(Long orderId) {
        RejectInfoVo rejectInfoVo = new RejectInfoVo();
        LambdaQueryWrapper<OrderRejectionOperate> orderRejectionOperateLambdaQueryWrapper = new LambdaQueryWrapper<>();
        orderRejectionOperateLambdaQueryWrapper.eq(OrderRejectionOperate::getOrderId,orderId)
                .orderByDesc(OrderRejectionOperate::getCreateTime)
                .last("LIMIT 1");
        OrderRejectionOperate orderRejectionOperateDB = this.getOne(orderRejectionOperateLambdaQueryWrapper);
        BeanUtils.copyProperties(orderRejectionOperateDB,rejectInfoVo);
        return rejectInfoVo;
    }
}
