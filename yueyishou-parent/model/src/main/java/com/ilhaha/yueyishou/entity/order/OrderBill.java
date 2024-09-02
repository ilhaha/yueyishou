package com.ilhaha.yueyishou.entity.order;


import com.baomidou.mybatisplus.annotation.TableName;
import com.ilhaha.yueyishou.entity.base.BaseEntity;
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


	/**订单ID*/
    private Long orderId;
	/**回收员预支付客户订单金额*/
    private BigDecimal expectCustomerAmount;
	/**回收员实际支付客户订单金额*/
    private BigDecimal realCustomerAmount;
	/**回收员预支付平台订单金额*/
    private BigDecimal expectPlatformAmount;
	/**回收员实际支付平台订单金额*/
    private BigDecimal realPlatformAmount;
	/**支付订单号*/
    private String transactionId;
	/**回收员付款时间*/
    private Date payTime;
}
