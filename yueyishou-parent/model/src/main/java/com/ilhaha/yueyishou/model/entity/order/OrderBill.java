package com.ilhaha.yueyishou.model.entity.order;

import com.baomidou.mybatisplus.annotation.TableName;
import com.ilhaha.yueyishou.model.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("order_bill")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class OrderBill extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 订单ID */
    private Long orderId;

    /** 订单实际回收总金额 */
    private BigDecimal realOrderRecycleAmount;

    /** 回收员实际支付订单金额 */
    private BigDecimal realRecyclerAmount;

    /** 回收员实际支付平台订单金额 */
    private BigDecimal realRecyclerPlatformAmount;

    /** 回收员开始服务超时费用 */
    private BigDecimal recyclerOvertimeCharges;

    /** 顾客实际回收订单金额 */
    private BigDecimal realCustomerRecycleAmount;

    /** 顾客实际支付平台订单金额 */
    private BigDecimal realCustomerPlatformAmount;

    /** 回收员服务抵扣券抵扣金额 */
    private BigDecimal recyclerCouponAmount;

    /** 顾客服务抵扣券抵扣金额 */
    private BigDecimal customerCouponAmount;

    /** 支付订单号 */
    private String transactionId;

    /** 回收员付款时间 */
    private Date payTime;

}
