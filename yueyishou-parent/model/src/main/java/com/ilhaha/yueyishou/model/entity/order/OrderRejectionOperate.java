package com.ilhaha.yueyishou.model.entity.order;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ilhaha.yueyishou.model.entity.base.BaseEntity;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 订单拒收申请操作记录表实体类
 */
@Data
@TableName("order_rejection_operate")
public class OrderRejectionOperate extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 驳回原因
     */
    private String reason;

    /**
     * 驳回佐证
     */
    private String proof;

    /**
     * 操作人
     */
    private String operator;
}