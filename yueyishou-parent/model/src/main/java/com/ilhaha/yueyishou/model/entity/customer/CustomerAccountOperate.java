package com.ilhaha.yueyishou.model.entity.customer;

import co.elastic.clients.elasticsearch.xpack.usage.Base;
import com.ilhaha.yueyishou.model.entity.base.BaseEntity;
import lombok.Data;
import java.io.Serializable;
import java.util.Date;

/**
 * 顾客账户操作记录实体类
 */
@Data
public class CustomerAccountOperate extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 顾客ID
     */
    private Long customerId;

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