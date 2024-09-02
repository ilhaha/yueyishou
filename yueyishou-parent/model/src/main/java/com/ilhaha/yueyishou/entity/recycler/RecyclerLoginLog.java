package com.ilhaha.yueyishou.entity.recycler;

import com.baomidou.mybatisplus.annotation.TableName;
import com.ilhaha.yueyishou.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@TableName("recycler_login_log")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class RecyclerLoginLog extends BaseEntity {
    private static final long serialVersionUID = 1L;

	/**回收员id*/
    private Long recyclerId;
	/**登录IP地址*/
    private String ipaddr;
	/**登录状态（0成功 1失败）*/
    private Integer status;
	/**提示信息*/
    private String msg;

}
