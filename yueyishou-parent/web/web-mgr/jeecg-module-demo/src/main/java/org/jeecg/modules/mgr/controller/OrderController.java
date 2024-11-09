package org.jeecg.modules.mgr.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ilhaha.yueyishou.model.entity.order.OrderInfo;
import com.ilhaha.yueyishou.model.form.order.ApprovalRejectOrderForm;
import com.ilhaha.yueyishou.model.form.order.OrderMgrQueryForm;
import com.ilhaha.yueyishou.model.vo.order.OrderMgrQueryVo;
import com.ilhaha.yueyishou.model.vo.order.RejectOrderListVo;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.mgr.service.OrderService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @Author ilhaha
 * @Create 2024/9/11 14:32
 * @Version 1.0
 */
@RestController
@RequestMapping("/order")
public class OrderController {

    @Resource
    private OrderService orderService;

    /**
     * 审批拒收申请
     * @param approvalRejectOrderForm
     * @return
     */
    @PostMapping("/approval/reject")
    public Result<Boolean> approvalReject(@RequestBody ApprovalRejectOrderForm approvalRejectOrderForm){
        return Result.OK(orderService.approvalReject(approvalRejectOrderForm));
    }

    /**
     * 获取申请拒收订单列表
     * @param pageNo
     * @param pageSize
     * @return
     */
    @PostMapping("/reject/list")
    public Result<Page<RejectOrderListVo>> getRejectOrderList(@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                              @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        return Result.OK(orderService.getRejectOrderList(pageNo,pageSize));
    }

    /**
     * 订单分页列表查询
     *
     * @param orderMgrQueryForm
     * @param pageNo
     * @param pageSize
     * @return
     */
    @GetMapping(value = "/list")
    public Result<Page<OrderMgrQueryVo>> queryPageList(OrderMgrQueryForm orderMgrQueryForm,
                                                       @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                       @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        return Result.OK(orderService.queryPageList(orderMgrQueryForm, pageNo, pageSize));
    }


    /**
     * 通过id查询订单信息
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/queryById")
    public Result<OrderInfo> queryById(@RequestParam(name = "id", required = true) String id) {
        return Result.OK(orderService.queryById(id));
    }

}
