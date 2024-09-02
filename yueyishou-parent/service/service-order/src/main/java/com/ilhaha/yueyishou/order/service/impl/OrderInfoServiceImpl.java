package com.ilhaha.yueyishou.order.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ilhaha.yueyishou.entity.order.OrderInfo;
import com.ilhaha.yueyishou.order.mapper.OrderInfoMapper;
import com.ilhaha.yueyishou.order.service.IOrderInfoService;
import org.springframework.stereotype.Service;

@Service
public class OrderInfoServiceImpl extends ServiceImpl<OrderInfoMapper, OrderInfo> implements IOrderInfoService {

}
