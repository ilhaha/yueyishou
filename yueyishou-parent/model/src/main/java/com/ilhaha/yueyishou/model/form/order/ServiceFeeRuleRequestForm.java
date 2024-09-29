package com.ilhaha.yueyishou.model.form.order;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ServiceFeeRuleRequestForm {

    /**
     * 单价
     */
    private BigDecimal unitPrice;

    /**
     * 重量
     */
    private BigDecimal recycleWeigh;

}