package com.ilhaha.yueyishou.model.vo.recycler;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author ilhaha
 * @Create 2024/10/22 14:15
 * @Version 1.0
 *
 * 回收员账户明细Vo
 */
@Data
public class RecyclerAccountDetailVo {

    /**账户明细id*/
    private Long id;
    /**交易内容*/
    private String content;
    /**交易类型：1回收支付费用 2提现*/
    private String tradeType;
    /**金额*/
    private BigDecimal amount;
    /**创建时间*/
    private Date createTime;
}
