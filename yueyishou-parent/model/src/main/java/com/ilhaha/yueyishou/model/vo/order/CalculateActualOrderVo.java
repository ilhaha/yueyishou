package com.ilhaha.yueyishou.model.vo.order;

import com.ilhaha.yueyishou.model.vo.coupon.AvailableCouponVo;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Author ilhaha
 * @Create 2024/10/18 09:28
 * @Version 1.0
 *
 * 计算实际订单信息
 */
@Data
public class CalculateActualOrderVo {

    /**订单ID**/
    private Long orderId;
    /**回收员实际支付订单金额*/
    private BigDecimal realRecyclerAmount;
    /**回收员实际支付平台订单金额*/
    private BigDecimal realRecyclerPlatformAmount;
    /**顾客实际支付平台订单金额*/
    private BigDecimal realCustomerPlatformAmount;
    /**回收员超时时间/分钟*/
    private Integer timeOutMin;
    /**回收员开始服务超时费用*/
    private BigDecimal recyclerOvertimeCharges;
    /**回收员可用服务抵扣劵*/
    private List<AvailableCouponVo> recyclerAvailableCouponList;
    /**实际回收员总共回收支出金额*/
    private BigDecimal totalAmount;

}
