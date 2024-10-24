package com.ilhaha.yueyishou.model.form.order;

import lombok.Data;

/**
 * @Author ilhaha
 * @Create 2024/10/24 14:37
 * @Version 1.0
 *
 * 顾客评论表单类
 */
@Data
public class ReviewForm {

    /**订单ID*/
    private Long orderId;
    /**回收员ID*/
    private Long recyclerId;
    /**顾客ID*/
    private Long customerId;
    /**评分，1星~5星*/
    private Integer rate;
    /**评论内容*/
    private String reviewContent;
}
