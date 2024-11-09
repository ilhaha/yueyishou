package com.ilhaha.yueyishou.model.form.order;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Author ilhaha
 * @Create 2024/11/8 16:54
 * @Version 1.0
 *
 * 审批回收员拒收订单Form
 */
@Data
public class ApprovalRejectOrderForm {

    /**
     * 拒收状态
     */
    private Integer rejectStatus;

    /**
     * 订单id
     */
    private Long orderId;

    /**
     * 顾客id
     */
    private Long customerId;

    /**
     * 回收员Id
     */
    private Long recyclerId;

    /**
     * 回收员拒收得到补偿
     */
    private BigDecimal rejectCompensation;

}
