package com.ilhaha.yueyishou.order.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ilhaha.yueyishou.model.entity.order.OrderBill;
import com.ilhaha.yueyishou.order.mapper.OrderBillMapper;
import com.ilhaha.yueyishou.order.service.IOrderBillService;
import org.springframework.stereotype.Service;

@Service
public class OrderBillServiceImpl extends ServiceImpl<OrderBillMapper, OrderBill> implements IOrderBillService {

}
