package org.jeecg.modules.mgr.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ilhaha.yueyishou.model.entity.order.OrderInfo;
import com.ilhaha.yueyishou.model.entity.order.OrderRejectionOperate;
import com.ilhaha.yueyishou.model.form.order.ApprovalRejectOrderForm;
import com.ilhaha.yueyishou.model.form.order.OrderMgrQueryForm;
import com.ilhaha.yueyishou.model.form.order.RejectOrderListForm;
import com.ilhaha.yueyishou.model.vo.order.RejectOrderListVo;
import com.ilhaha.yueyishou.order.client.OrderInfoFeignClient;
import com.ilhaha.yueyishou.model.vo.order.OrderMgrQueryVo;
import com.ilhaha.yueyishou.order.client.OrderRejectionOperateFeignClient;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.mgr.service.OrderService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author ilhaha
 * @Create 2024/9/11 14:32
 * @Version 1.0
 */
@Service
public class OrderServiceImpl implements OrderService {

    @Resource
    private OrderInfoFeignClient orderInfoFeignClient;

    @Resource
    private OrderRejectionOperateFeignClient orderRejectionOperateFeignClient;

    /**
     * 订单分页查询
     * @param orderMgrQueryForm
     * @param pageNo
     * @param pageSize
     * @return
     */
    @Override
    public Page<OrderMgrQueryVo> queryPageList(OrderMgrQueryForm orderMgrQueryForm, Integer pageNo, Integer pageSize) {
        return orderInfoFeignClient.queryPageList(orderMgrQueryForm,pageNo,pageSize).getData();
    }

    /**
     * 通过id查询订单信息
     *
     * @param id
     * @return
     */
    @Override
    public OrderInfo queryById(String id) {
        return orderInfoFeignClient.queryById(id).getData();
    }

    /**
     * 获取申请拒收订单列表
     *
     * @param pageNo
     * @param pageSize
     * @param rejectOrderListForm
     * @return
     */
    @Override
    public Page<RejectOrderListVo> getRejectOrderList(Integer pageNo, Integer pageSize, RejectOrderListForm rejectOrderListForm) {
        return orderInfoFeignClient.getRejectOrderList(pageNo,pageSize,rejectOrderListForm).getData();
    }

    /**
     * 审批拒收申请
     * @param approvalRejectOrderForm
     * @return
     */
    @Override
    public Boolean approvalReject(ApprovalRejectOrderForm approvalRejectOrderForm) {
        return orderInfoFeignClient.approvalReject(approvalRejectOrderForm).getData();
    }


    /**
     * 添加审核订单拒收操作记录
     * @param rejectionOperate
     * @return
     */
    @Override
    public Boolean add(OrderRejectionOperate rejectionOperate) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        rejectionOperate.setOperator(sysUser.getRealname());
        return orderRejectionOperateFeignClient.add(rejectionOperate).getData();
    }
}
