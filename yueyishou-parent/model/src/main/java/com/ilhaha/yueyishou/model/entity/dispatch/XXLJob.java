package com.ilhaha.yueyishou.model.entity.dispatch;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ilhaha.yueyishou.model.entity.base.BaseEntity;
import lombok.Data;

@Data
@TableName("XXL_job")
public class XXLJob extends BaseEntity {

	private static final long serialVersionUID = 1L;


	/**
	 * job_id
	 */
	@TableField("job_id")
	private Long jobId;

	/**
	 * 任务名称
	 */
	@TableField("task_name")
	private String taskName;

	/**
	 * 参数
	 */
	@TableField("parameter")
	private String parameter;

}