package com.ilhaha.yueyishou.model.entity.recycler;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ilhaha.yueyishou.model.entity.base.BaseEntity;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 实体类：RecyclerExamineOperate
 * 表名：recycler_examine_operate
 * 描述：认证回收员审核操作记录表
 */
@Data
@TableName("recycler_examine_operate")
public class RecyclerExamineOperate extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 回收员ID
     */
    private Long recyclerId;

    /**
     * 审核原因
     */
    private String reason;

    /**
     * 审核佐证
     */
    private String proof;

    /**
     * 操作人
     */
    private String operator;

    /**
     * 审核状态：
     * 1 - 重新提交
     * 2 - 通过
     * -1 - 未通过
     */
    private Integer examineStatus;

}