package com.ilhaha.yueyishou.model.vo.recycler;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Author ilhaha
 * @Create 2024/10/22 14:11
 * @Version 1.0
 *
 * 回收员账户Vo
 */
@Data
public class RecyclerAccountVo {

    /**账户总金额*/
    private BigDecimal totalAmount;
    /**回收总支出*/
    private BigDecimal totalRecyclePay;
    /**账户交易明细*/
    private List<RecyclerAccountDetailVo> recyclerAccountDetailVoList;

}
