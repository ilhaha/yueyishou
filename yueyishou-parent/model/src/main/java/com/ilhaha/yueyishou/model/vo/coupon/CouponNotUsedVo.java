package com.ilhaha.yueyishou.model.vo.coupon;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author ilhaha
 * @Create 2024/10/29 22:49
 * @Version 1.0
 *
 * 未使用的服务抵扣劵Vo
 */
@Data
public class CouponNotUsedVo {

    /**服务抵扣劵Id*/
    private Long couponId;
    /**服务费抵扣券类型 1 折扣 2 免单*/
    private Integer couponType;
    /**服务费抵扣券名字*/
    private String name;
    /**使用门槛 0->没门槛*/
    private BigDecimal conditionAmount;
    /**服务费抵扣券描述*/
    private String description;
    /**过期时间*/
    private Date expireTime;

}
