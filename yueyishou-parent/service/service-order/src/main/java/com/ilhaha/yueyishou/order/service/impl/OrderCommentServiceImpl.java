package com.ilhaha.yueyishou.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ilhaha.yueyishou.model.entity.order.OrderComment;
import com.ilhaha.yueyishou.model.enums.OrderStatus;
import com.ilhaha.yueyishou.model.form.order.ReviewForm;
import com.ilhaha.yueyishou.model.form.recycler.UpdateRateForm;
import com.ilhaha.yueyishou.model.vo.order.OrderCommentVo;
import com.ilhaha.yueyishou.order.mapper.OrderCommentMapper;
import com.ilhaha.yueyishou.order.service.IOrderCommentService;
import com.ilhaha.yueyishou.order.service.IOrderInfoService;
import com.ilhaha.yueyishou.recycler.client.RecyclerInfoFeignClient;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.util.List;

@Service
public class OrderCommentServiceImpl extends ServiceImpl<OrderCommentMapper, OrderComment> implements IOrderCommentService {

    @Resource
    private RecyclerInfoFeignClient recyclerInfoFeignClient;

    @Resource
    private IOrderInfoService orderInfoService;

    /**
     * 顾客评论
     * @return
     */
    @Override
    public Boolean review(ReviewForm reviewForm) {
        OrderComment orderComment = new OrderComment();
        BeanUtils.copyProperties(reviewForm,orderComment);
        boolean flag = this.save(orderComment);
        // 评论添加成功，重新计算回收员评分
        if (flag) {
            // 获取该回收员所有的评论信息
            LambdaQueryWrapper<OrderComment> orderCommentLambdaQueryWrapper = new LambdaQueryWrapper<>();
            orderCommentLambdaQueryWrapper.eq(OrderComment::getRecyclerId,reviewForm.getRecyclerId());
            List<OrderComment> orderCommentListDB = this.list(orderCommentLambdaQueryWrapper);
            // 计算评分平均值
            double averageRating = orderCommentListDB.stream()
                    .mapToInt(OrderComment::getRate)
                    .average()
                    .orElse(0.0);

            // 更新回收员的评分信息
            UpdateRateForm updateRateForm = new UpdateRateForm();
            updateRateForm.setRate(BigDecimal.valueOf(averageRating));
            updateRateForm.setRecyclerId(reviewForm.getRecyclerId());
            recyclerInfoFeignClient.updateRate(updateRateForm);

            // 修改订单状态
            orderInfoService.updateOrderStatus(reviewForm.getOrderId(), OrderStatus.AWAITING_EVALUATION.getStatus());
        }
        return flag;
    }

    /**
     * 查询订单列表评论信息
     * @param orderId
     * @return
     */
    @Override
    public OrderCommentVo getOrderComment(Long orderId) {
        OrderCommentVo orderCommentVo = new OrderCommentVo();
        OrderComment orderCommentDB = this.getOne(new LambdaQueryWrapper<OrderComment>().eq(OrderComment::getOrderId, orderId));
        if (!ObjectUtils.isEmpty(orderCommentDB)) {
            orderCommentVo.setReviewContent(orderCommentDB.getReviewContent());
            orderCommentVo.setReviewTime(orderCommentDB.getCreateTime());
            orderCommentVo.setRate(orderCommentDB.getRate());
        }
        return orderCommentVo;
    }
}
