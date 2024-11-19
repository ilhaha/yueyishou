package com.ilhaha.yueyishou.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ilhaha.yueyishou.model.entity.order.OrderRejectionOperate;
import com.ilhaha.yueyishou.model.vo.order.RejectInfoVo;

/**
 * @Author ilhaha
 * @Create 2024/11/19 11:49
 * @Version 1.0
 */
public interface IOrderRejectionOperateService extends IService<OrderRejectionOperate> {

    /**
     * 添加审核订单拒收操作记录
     * @param rejectionOperate
     * @return
     */
    Boolean add(OrderRejectionOperate rejectionOperate);

    /**
     * 获取回收员申请拒收订单被驳回反馈信息
     * @param orderId
     * @return
     */
    RejectInfoVo rejectFeedback(Long orderId);
}
