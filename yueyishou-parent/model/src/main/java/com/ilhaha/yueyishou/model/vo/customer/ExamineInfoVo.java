package com.ilhaha.yueyishou.model.vo.customer;

import lombok.Data;

/**
 * @Author ilhaha
 * @Create 2024/11/19 10:46
 * @Version 1.0
 *
 * 顾客认证回收员的审核反馈Vo
 */
@Data
public class ExamineInfoVo {

    /**
     * 审核原因
     */
    private String reason;

    /**
     * 审核佐证
     */
    private String proof;
}
