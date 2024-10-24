package com.ilhaha.yueyishou.model.vo.order;

import lombok.Data;

import java.util.Date;

/**
 * @Author ilhaha
 * @Create 2024/10/24 15:37
 * @Version 1.0
 *
 * 订单评论Vo
 */
@Data
public class OrderCommentVo {

    /**评论内容*/
    private String reviewContent;

    /**评论时间*/
    private Date reviewTime;

}
