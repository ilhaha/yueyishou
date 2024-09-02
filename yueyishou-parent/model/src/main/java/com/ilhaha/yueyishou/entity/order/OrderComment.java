package com.ilhaha.yueyishou.entity.order;


import com.baomidou.mybatisplus.annotation.TableName;
import com.ilhaha.yueyishou.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@TableName("order_comment")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class OrderComment extends BaseEntity {
    private static final long serialVersionUID = 1L;

	/**订单ID*/
    private Long orderId;
	/**回收员ID*/
    private Long recyclerId;
	/**顾客ID*/
    private Long customerId;
	/**评分，1星~5星*/
    private Integer rate;
	/**备注*/
    private String remark;

}
