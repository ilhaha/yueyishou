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
    /**实物照片*/
    private String actualPhoto;
    /**回收重量*/
    private BigDecimal recycleWeigh;
    /**订单预计回收总金额*/
    private BigDecimal estimatedTotalAmount;
    /**订单状态：1等待接单，2回收员已接单，3待顾客确认，4回收员未付款，5回收员已付款待评价，6已完成，7订单已取消*/
    private Integer status;
    /**顾客预支付平台订单金额*/
    private BigDecimal expectCustomerPlatformAmount;
}
