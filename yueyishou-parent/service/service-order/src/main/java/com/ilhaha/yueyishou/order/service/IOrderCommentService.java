package com.ilhaha.yueyishou.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ilhaha.yueyishou.model.entity.order.OrderComment;
import com.ilhaha.yueyishou.model.form.order.ReviewForm;
import com.ilhaha.yueyishou.model.vo.order.OrderCommentVo;

public interface IOrderCommentService extends IService<OrderComment> {

    /**
     * 顾客评论
     * @return
     */
    Boolean review(ReviewForm reviewForm);

    /**
     * 查询订单列表评论信息
     * @param orderId
     * @return
     */
    OrderCommentVo getOrderComment(Long orderId);
}
