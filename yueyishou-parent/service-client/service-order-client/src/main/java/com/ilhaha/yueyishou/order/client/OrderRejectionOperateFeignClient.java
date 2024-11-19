package com.ilhaha.yueyishou.order.client;

import com.ilhaha.yueyishou.common.result.Result;
import com.ilhaha.yueyishou.model.entity.order.OrderRejectionOperate;
import com.ilhaha.yueyishou.model.vo.order.RejectInfoVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @Author ilhaha
 * @Create 2024/11/19 11:52
 * @Version 1.0
 */
@FeignClient("service-order")
public interface OrderRejectionOperateFeignClient {

    /**
     * 获取回收员申请拒收订单被驳回反馈信息
     * @param orderId
     * @return
     */
    @GetMapping("/orderRejectionOperate/reject/feedback/{orderId}")
    Result<RejectInfoVo> rejectFeedback(@PathVariable("orderId") Long orderId);

    /**
     * 添加审核订单拒收操作记录
     * @param rejectionOperate
     * @return
     */
    @PostMapping("/orderRejectionOperate/add")
    Result<Boolean> add(@RequestBody OrderRejectionOperate rejectionOperate);
}
