package com.ilhaha.yueyishou.model.vo.customer;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author ilhaha
 * @Create 2024/10/23 23:03
 * @Version 1.0
 *
 * 顾客账户明细Vo
 */
@Data
public class CustomerAccountDetailVo {

    /**账户明细id*/
    private Long id;
    /**交易内容*/
    private String content;
    /**交易类型： 1-回收收入 2-提现*/
    private String tradeType;
    /**金额*/
    private BigDecimal amount;
    /**创建时间*/
    private Date createTime;
}
