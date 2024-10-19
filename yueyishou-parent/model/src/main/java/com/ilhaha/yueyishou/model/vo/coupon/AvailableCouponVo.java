package com.ilhaha.yueyishou.model.vo.coupon;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author ilhaha
 * @Create 2024/10/18 14:45
 * @Version 1.0
 *
 * 回收员、顾客可使用的服务抵扣劵Vo
 */
@Data
public class AvailableCouponVo {
    /**服务抵扣劵id*/
    private Long id;
    /**服务费抵扣券类型 1 折扣 2 免单*/
    private Integer couponType;
    /**服务费抵扣券名字*/
    private String name;
    /**折扣：取值[0 到 10]*/
    private BigDecimal discount;
    /**使用门槛 0->没门槛*/
    private BigDecimal conditionAmount;
    /**服务费抵扣券描述*/
    private String description;
}
