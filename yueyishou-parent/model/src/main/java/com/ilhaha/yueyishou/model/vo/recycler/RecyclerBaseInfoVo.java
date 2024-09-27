package com.ilhaha.yueyishou.model.vo.recycler;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author ilhaha
 * @Create 2024/9/26 14:53
 * @Version 1.0
 */
@Data
public class RecyclerBaseInfoVo {

    /**头像*/
    private String avatarUrl;

    /**姓名*/
    private String name;

    /**回收类型，多个使用逗号隔开*/
    private String recyclingType;

    /**服务状态  0：未接单 1：开始接单 2：有单中*/
    private Integer serviceStatus;

    /**接单里程设置*/
    private BigDecimal acceptDistance;

}
