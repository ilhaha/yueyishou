package com.ilhaha.yueyishou.model.vo.order;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author ilhaha
 * @Create 2024/10/24 21:33
 * @Version 1.0
 * <p>
 * 取消订单Vo
 */
@Data
public class CancelOrderVo {

    /**
     * 接单后，回收员在预约时间未到达取消订单赔偿
     */
    private BigDecimal serviceOvertimePenalty;

    /**
     * 接单后，顾客未在免费取消订单时间内取消赔偿
     */
    private BigDecimal customerLateCancellationFee;

    /**
     * 接单后，回收员未在免费取消订单时间内取消赔偿
     */
    private BigDecimal recyclerLateCancellationFee;

    /**
     * 超时分钟数
     */
    private Integer overtimeMinutes;

}
