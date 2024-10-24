package com.ilhaha.yueyishou.model.entity.recycler;

import com.baomidou.mybatisplus.annotation.TableName;
import com.ilhaha.yueyishou.model.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@TableName("recycler_personalization")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class RecyclerPersonalization extends BaseEntity {
    private static final long serialVersionUID = 1L;

	/**回收员ID*/
    private Long recyclerId;
	/**回收类型，多个使用逗号隔开*/
    private String recyclingType;
	/**服务状态  0：未接单 1：开始接单 2：有单中*/
    private Integer serviceStatus;
	/**接单里程设置*/
    private BigDecimal acceptDistance;

}
