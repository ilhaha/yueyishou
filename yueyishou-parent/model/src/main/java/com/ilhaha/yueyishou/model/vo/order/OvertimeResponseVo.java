package com.ilhaha.yueyishou.model.vo.order;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 计算回收员超时费用Vo
 */
@Data
public class OvertimeResponseVo {

    /**
     * 超时费用
     */
    private BigDecimal overtimeFee;
}