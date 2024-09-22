package com.ilhaha.yueyishou.model.entity.customer;

import com.baomidou.mybatisplus.annotation.TableName;
import com.ilhaha.yueyishou.model.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@TableName("customer_account_detail")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class CustomerAccountDetail extends BaseEntity {
    private static final long serialVersionUID = 1L;

	/**顾客账户id*/
    private Long customerAccountId;
	/**交易内容*/
    private String content;
	/**交易类型： 1-回收收入 2-提现*/
    private String tradeType;
	/**金额*/
    private BigDecimal amount;
	/**交易编号*/
    private String tradeNo;

}
