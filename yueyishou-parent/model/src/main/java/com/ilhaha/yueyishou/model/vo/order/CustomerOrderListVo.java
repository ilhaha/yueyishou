package com.ilhaha.yueyishou.model.vo.order;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author ilhaha
 * @Create 2024/9/29 15:01
 * @Version 1.0
 */
@Data
public class CustomerOrderListVo {

    /**订单id*/
    private Long id;
    /**订单回收分类子名称*/
    private String sonCategoryName;
    /**订单回收分类父名称*/
    private String parentCategoryName;
    /**单价*/
    private BigDecimal unitPrice;
    /**预约时间*/
    private Date appointmentTime;
    /**回收员到达时间*/
    private Date arriveTime;
    /**实物照片*/
    private String actualPhoto;
    /**回收重量*/
    private BigDecimal recycleWeigh;
    /**订单预计回收总金额*/
    private BigDecimal estimatedTotalAmount;
    /**订单状态：1等待接单，2回收员已接单，3待回收员确认，4待顾客确认，5回收员未付款，6回收员已付款待评价，7已完成，8订单已取消*/
    private Integer status;
    /**顾客预支付平台订单金额*/
    private BigDecimal expectCustomerPlatformAmount;
    /** 订单实际回收总金额 */
    private BigDecimal realOrderRecycleAmount;
    /** 顾客实际支付平台订单金额 */
    private BigDecimal realCustomerPlatformAmount;
    /** 回收员开始服务超时费用 */
    private BigDecimal recyclerOvertimeCharges;
    /**回收员超时多少分钟*/
    private Integer arriveTimoutMin;
    /** 顾客实际回收订单金额 */
    private BigDecimal realCustomerRecycleAmount;
    /** 回收员付款时间 */
    private Date payTime;
    /**评论内容*/
    private String reviewContent;
    /**评论时间*/
    private Date reviewTime;
    /**取消时间*/
    private Date cancelTime;
    /**接单后，回收员在预约时间未到达取消订单赔偿*/
    private BigDecimal serviceOvertimePenalty;
    /**接单后，顾客未在免费取消订单时间内取消赔偿*/
    private BigDecimal customerLateCancellationFee;
    /**接单后，回收员未在免费取消订单时间内取消赔偿*/
    private BigDecimal recyclerLateCancellationFee;
    /**
     * 拒收状态，0-不拒收 1-拒收 2-已拒收 1-拒收未通过
     */
    private Integer rejectStatus;
    /**
     * 回收员拒收得到补偿
     */
    private BigDecimal rejectCompensation;
}
