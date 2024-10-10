package com.ilhaha.yueyishou.model.constant;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author ilhaha
 * @Create 2024/9/26 15:53
 * @Version 1.0
 */
@Data
public class PersonalizationConstant {
    /**
     * 服务状态：停止接单
     */
    public static final Integer STOP_TAKING_ORDERS = 0;

    /**
     * 服务状态：接单中
     */
    public static final Integer START_TAKING_ORDERS = 1;

    /**
     * 服务状态：有单中
     */
    public static final Integer TAKING_ORDERS = 2;

    /**
     * 默认接单里程
     */
    public static final BigDecimal DEFAULT_DISTANCE = new BigDecimal(5.0);
}
