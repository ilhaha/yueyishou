package com.ilhaha.yueyishou.model.entity.recycler;

import com.baomidou.mybatisplus.annotation.TableName;
import com.ilhaha.yueyishou.model.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@TableName("recycler_account_detail")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class RecyclerAccountDetail extends BaseEntity {
    private static final long serialVersionUID = 1L;

	/**回收员账户id*/
    private Long recyclerAccountId;
	/**交易内容*/
    private String content;
	/**交易类型：1回收支付费用 2提现 3充值*/
    private String tradeType;
	/**金额*/
    private BigDecimal amount;
	/**交易编号*/
    private String tradeNo;
}
