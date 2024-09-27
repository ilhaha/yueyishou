package com.ilhaha.yueyishou.model.entity.order;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("order_info")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class OrderInfo implements Serializable {
    private static final long serialVersionUID = 1L;

	/**客户ID*/
    private Long customerId;
	/**订单号*/
    private String orderNo;
	/**客户地点*/
    private String customerLocation;
	/**客户地点经度*/
    private BigDecimal customerPointLongitude;
	/**客户地点伟度*/
    private BigDecimal customerPointLatitude;
	/**订单回收分类ID*/
    private Integer categoryId;
    /**预约回收开始时间*/
    private Date appointmentStartTime;
    /**预约回收结束时间*/
    private Date appointmentEndTime;
    /**实物照片*/
    private String actualPhotos;
	/**回收员ID*/
    private Integer recyclerId;
	/**回收员接单时间*/
    private Date acceptTime;
	/**回收员到达时间*/
    private Date arriveTime;
	/**订单状态：1等待接单，2回收员已接单，3回收员前往回收点，4待顾客确认，5回收员未付款，6回收员已付款订单完成，7待评价，8订单已取消*/
    private Integer status;
	/**取消订单信息*/
    private String cancelMessage;
	/**订单备注信息*/
    private String remark;
    /**订单联系人*/
    private String orderContactPerson;
    /**订单联系电话*/
    private String orderContactPhone;
	/**回收员预支付订单金额*/
    private BigDecimal expectRecyclerAmount;
	/**回收员预支付平台订单金额*/
    private BigDecimal expectRecyclerPlatformAmount;
	/**顾客预支付平台订单金额*/
    private BigDecimal expectCustomerPlatformAmount;
}
