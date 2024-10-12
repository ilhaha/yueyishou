package com.ilhaha.yueyishou.model.vo.order;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author ilhaha
 * @Create 2024/9/28 21:15
 * @Version 1.0
 */
@Data
public class PlaceOrderForm {
    /**客户ID*/
    private Long customerId;
    /**订单号*/
    private String orderNo;
    /**客户地点*/
    private String customerLocation;
    /**客户地点经度*/
    private BigDecimal customerPointLongitude;
    /**客户地点纬度*/
    private BigDecimal customerPointLatitude;
    /**订单回收分类父ID*/
    private Long parentCategoryId;
    /**订单回收分类父名称*/
    private String parentCategoryName;
    /**订单回收分类子ID*/
    private Long sonCategoryId;
    /**订单回收分类子名称*/
    private String sonCategoryName;
    /**单价*/
    private BigDecimal unitPrice;
    /**预约时间*/
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private Date appointmentTime;
    /**实物照片*/
    private String actualPhotos;
    /**回收重量*/
    private BigDecimal recycleWeigh;
    /**订单状态：1等待接单，2回收员已接单，3回收员前往回收点，4待顾客确认，5回收员未付款，6回收员已付款订单完成，7待评价，8订单已取消*/
    private Integer status;
    /**订单备注信息*/
    private String remark;
    /**订单联系人*/
    private String orderContactPerson;
    /**订单联系电话*/
    private String orderContactPhone;
    /**订单预计回收总金额*/
    private BigDecimal estimatedTotalAmount;
    /**回收员预支付平台订单金额*/
    private BigDecimal expectRecyclerPlatformAmount;
    /**顾客预支付平台订单金额*/
    private BigDecimal expectCustomerPlatformAmount;

}
