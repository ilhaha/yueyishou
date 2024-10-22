package com.ilhaha.yueyishou.model.entity.recycler;


import com.baomidou.mybatisplus.annotation.TableName;
import com.ilhaha.yueyishou.model.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@TableName("recycler_account")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class RecyclerAccount extends BaseEntity {
    private static final long serialVersionUID = 1L;


	/**回收员id*/
    private Long recyclerId;
	/**账户总金额*/
    private BigDecimal totalAmount;
	/**回收总支出*/
    private BigDecimal totalRecyclePay;
}
