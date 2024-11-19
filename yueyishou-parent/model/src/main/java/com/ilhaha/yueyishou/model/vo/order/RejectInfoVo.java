package com.ilhaha.yueyishou.model.vo.order;

import lombok.Data;

/**
 * @Author ilhaha
 * @Create 2024/11/19 10:46
 * @Version 1.0
 *
 * 回收员申请拒收订单被驳回反馈Vo
 */
@Data
public class RejectInfoVo {

    /**
     * 驳回原因
     */
    private String reason;

    /**
     * 审核佐证
     */
    private String proof;
}
