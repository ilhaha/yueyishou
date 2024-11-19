package com.ilhaha.yueyishou.model.entity.recycler;

import com.ilhaha.yueyishou.model.entity.base.BaseEntity;
import lombok.Data;

/**
 * 回收员账户操作记录实体类
 */
@Data
public class RecyclerAccountOperate extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 回收员ID
     */
    private Long recyclerId;

    /**
     * 操作原因
     */
    private String reason;

    /**
     * 操作佐证
     */
    private String proof;

    /**
     * 操作人
     */
    private String operator;
}