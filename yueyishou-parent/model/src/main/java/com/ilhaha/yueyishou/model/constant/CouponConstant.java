package com.ilhaha.yueyishou.model.constant;

import lombok.Data;
import org.springframework.web.bind.annotation.GetMapping;

import java.math.BigDecimal;

/**
 * @Author ilhaha
 * @Create 2024/120/18 15:04
 * @Version 1.0
 * <p>
 * 服务抵扣劵常量
 */
@Data
public class CouponConstant {

    /**
     * 服务抵扣劵未使用状态
     */
    public final static Integer UNUSED_STATUS = 1;

    /**
     * 服务抵扣劵已使用状态
     */
    public final static Integer USED_STATUS = 2;

    /**
     * 使用门槛 0->没门槛
     */
    public final static Integer NO_THRESHOLD = 0;

    /**
     * 服务费抵扣券类型:折扣
     */
    public final static Integer DISCOUNT_TYPE = 1;

    /**
     * 服务费抵扣券类型:免单
     */
    public final static Integer FREE_CHARGE_TYPE = 2;

    /**
     * 服务费抵扣券领取后几天过期，0表示无期限
     */
    public final static Integer EXPIRES_DAYS_AFTER_COLLECTION = 0;


}
