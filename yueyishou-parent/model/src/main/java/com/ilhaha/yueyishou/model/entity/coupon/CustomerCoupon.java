package com.ilhaha.yueyishou.model.entity.coupon;

import com.baomidou.mybatisplus.annotation.TableName;
import com.ilhaha.yueyishou.model.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@TableName("customer_coupon")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class CustomerCoupon extends BaseEntity {
    private static final long serialVersionUID = 1L;

	/**服务费抵扣券ID*/
    private Long couponId;
	/**顾客ID*/
    private Long customerId;
	/**优化券状态（1：未使用 2：已使用）*/
    private Integer status;
	/**领取时间*/
    private Date receiveTime;
	/**使用时间*/
    private Date usedTime;
	/**使用的订单id*/
    private Integer orderId;
	/**过期时间*/
    private Date expireTime;
}
