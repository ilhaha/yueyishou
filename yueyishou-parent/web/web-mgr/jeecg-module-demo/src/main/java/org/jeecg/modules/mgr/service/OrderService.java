package org.jeecg.modules.mgr.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ilhaha.yueyishou.model.entity.order.OrderInfo;
import com.ilhaha.yueyishou.model.form.order.ApprovalRejectOrderForm;
import com.ilhaha.yueyishou.model.form.order.OrderMgrQueryForm;
import com.ilhaha.yueyishou.model.vo.order.OrderMgrQueryVo;
import com.ilhaha.yueyishou.model.vo.order.RejectOrderListVo;
import org.jeecg.common.api.vo.Result;

/**
 * @Author ilhaha
 * @Create 2024/9/11 14:32
 * @Version 1.0
 */
public interface OrderService {
    /**
     * 订单分页查询
     * @param orderMgrQueryForm
     * @param pageNo
     * @param pageSize
     * @return
     */
    Page<OrderMgrQueryVo> queryPageList(OrderMgrQueryForm orderMgrQueryForm, Integer pageNo, Integer pageSize);

    /**
     * 通过id查询订单信息
     *
     * @param id
     * @return
     */
    OrderInfo queryById(String id);

    /**
     * 获取申请拒收订单列表
     * @param pageNo
     * @param pageSize
     * @return
     */
    Page<RejectOrderListVo> getRejectOrderList(Integer pageNo, Integer pageSize);

    /**
     * 审批拒收申请
     * @param approvalRejectOrderForm
     * @return
     */
    Boolean approvalReject(ApprovalRejectOrderForm approvalRejectOrderForm);
}
