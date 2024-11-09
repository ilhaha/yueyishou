package com.ilhaha.yueyishou.model.vo.order;

import com.ilhaha.yueyishou.model.entity.order.OrderInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author ilhaha
 * @Create 2024/9/11 17:26
 * @Version 1.0
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class OrderMgrQueryVo extends OrderInfo {
    /**
     * Order 信息
     */
    private Long orderId;
    private String customerLocation;
    private Date appointmentTime;
    private String actualPhotos;
    private BigDecimal unitPrice;
    private BigDecimal recycleWeigh;
    private Date acceptTime;
    private Date arriveTime;
    private Integer status;
    private String cancelMessage;
    private Date cancelTime;
    private BigDecimal serviceOvertimePenalty;
    private BigDecimal customerLateCancellationFee;
    private BigDecimal recyclerLateCancellationFee;
    private String remark;
    private String orderContactPerson;
    private String orderContactPhone;
    private BigDecimal estimatedTotalAmount;
    private BigDecimal expectRecyclerPlatformAmount;
    private BigDecimal expectCustomerPlatformAmount;
    private Date createTime;
    private Integer arriveTimoutMin;
    private BigDecimal rejectCompensation;

    /**
     * Recycler 信息
     */
    private Long recyclerId;
    private String recyclerName;

    /**
     * Category 信息
     */
    private Long categoryId;
    private String categoryName;

    /**
     * Bill 信息
     */
    private Long billId;
    private BigDecimal realOrderRecycleAmount;
    private BigDecimal realRecyclerAmount;
    private BigDecimal realRecyclerPlatformAmount;
    private BigDecimal recyclerOvertimeCharges;
    private BigDecimal realCustomerRecycleAmount;
    private BigDecimal realCustomerPlatformAmount;
    private BigDecimal recyclerCouponAmount;
    private BigDecimal customerCouponAmount;
    private Date payTime;

    /**
     * Comment 信息
     */
    private Long commentId;
    private Integer rate;
    private String reviewContent;
    private Date commentTime;
}
