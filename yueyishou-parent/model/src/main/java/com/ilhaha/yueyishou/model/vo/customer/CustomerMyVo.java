package com.ilhaha.yueyishou.model.vo.customer;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author ilhaha
 * @Create 2024/10/29 21:25
 * @Version 1.0
 *
 * 顾客我的页面初始信息Vo
 */
@Data
public class CustomerMyVo {

    /**
     * 参与回收次数
     */
    private Integer recyclerCount;

    /**
     * 投递量
     */
    private BigDecimal deliveryVolume;

    /**
     * 账户余额
     */
    private BigDecimal accountBalance;
}
