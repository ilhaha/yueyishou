package com.ilhaha.yueyishou.entity.customer;

import com.baomidou.mybatisplus.annotation.TableName;
import com.ilhaha.yueyishou.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@TableName("customer_login_log")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class CustomerLoginLog extends BaseEntity {
    private static final long serialVersionUID = 1L;

	/**客户id*/
    private Long customerId;
	/**登录IP地址*/
    private String ipaddr;
	/**登录状态*/
    private Integer status;
	/**提示信息*/
    private String msg;
}
