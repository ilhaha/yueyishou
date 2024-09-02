package com.ilhaha.yueyishou.entity.order;


import com.baomidou.mybatisplus.annotation.TableName;
import com.ilhaha.yueyishou.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("order_info")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class OrderInfo extends BaseEntity {
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
    private Long categoryId;
	/**回收员ID*/
    private Long recyclerId;
	/**回收员接单时间*/
    private Date acceptTime;
	/**回收员到达时间*/
    private Date arriveTime;
	/**订单状态：1等待接单，2回收员已接单，3回收员已到达，4顾客确定订单，5回收员未付款，6回收员已付款，7订单已完成，8已取消*/
    private Integer status;
	/**取消订单信息*/
    private String cancelMessage;
	/**订单备注信息*/
    private String remark;
}
