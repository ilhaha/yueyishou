package com.ilhaha.yueyishou.entity.coupon;

import com.baomidou.mybatisplus.annotation.TableName;
import com.ilhaha.yueyishou.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("coupon_info")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class CouponInfo extends BaseEntity {
    private static final long serialVersionUID = 1L;


	/**服务费抵扣券类型 1 折扣 2 免单*/
    private Integer couponType;
	/**服务费抵扣券名字*/
    private String name;
	/**折扣：取值[1 到 10]*/
    private BigDecimal discount;
	/**使用门槛 0->没门槛*/
    private BigDecimal conditionAmount;
	/**每人限领张数，0-不限制 1-限领1张 2-限领2张...*/
    private Integer perLimit;
	/**已使用数量*/
    private Integer useCount;
	/**领取数量*/
    private Integer receiveCount;
	/**领取后几天到期 0 无限制*/
    private Integer expireTime;
	/**服务费抵扣券描述*/
    private String description;
	/**状态[0-未发布，1-已发布， -1-已过期]*/
    private Integer status;
}
