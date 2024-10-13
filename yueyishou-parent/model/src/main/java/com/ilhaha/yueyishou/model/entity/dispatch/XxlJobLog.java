package com.ilhaha.yueyishou.model.entity.dispatch;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ilhaha.yueyishou.model.entity.base.BaseEntity;
import lombok.Data;

@Data
@TableName("xxl_job_log")
public class XxlJobLog extends BaseEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 任务id
	 */
	@TableField("job_id")
	private Long jobId;

	/**
	 * 任务状态    0：失败    1：成功
	 */
	@TableField("status")
	private Integer status;

	/**
	 * 失败信息
	 */
	@TableField("error")
	private String error;

	/**
	 * 耗时(单位：毫秒)
	 */
	@TableField("times")
	private Long times;

}