package com.ilhaha.yueyishou.model.vo.customer;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Author ilhaha
 * @Create 2024/10/23 23:01
 * @Version 1.0
 *
 * 顾客账户信息Vo
 */
@Data
public class CustomerAccountVo {

    /**账户总余额*/
    private BigDecimal totalAmount;
    /**回收总收入*/
    private BigDecimal totalRecycleIncome;
    /**账户交易明细*/
    private List<CustomerAccountDetailVo> customerAccountDetailVoList;
}
