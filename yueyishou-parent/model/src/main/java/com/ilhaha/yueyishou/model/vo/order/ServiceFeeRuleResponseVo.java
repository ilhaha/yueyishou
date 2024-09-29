package com.ilhaha.yueyishou.model.vo.order;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author ilhaha
 * @Create 2024/9/28 20:03
 * @Version 1.0
 */
@Data
public class ServiceFeeRuleResponseVo {

    /**
     * 回收员预支付平台订单金额
     */
    private BigDecimal expectRecyclerPlatformAmount;

    /**
     * 顾客预支付平台订单金额
     */
    private BigDecimal expectCustomerPlatformAmount;

    /**
     * 订单预计回收总金额
     */
    private BigDecimal estimatedTotalAmount;



}
