package com.ilhaha.yueyishou.order.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ilhaha.yueyishou.model.entity.order.OrderComment;
import com.ilhaha.yueyishou.order.mapper.OrderCommentMapper;
import com.ilhaha.yueyishou.order.service.IOrderCommentService;
import org.springframework.stereotype.Service;

@Service
public class OrderCommentServiceImpl extends ServiceImpl<OrderCommentMapper, OrderComment> implements IOrderCommentService {

}
