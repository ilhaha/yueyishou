package com.ilhaha.yueyishou.order.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ilhaha.yueyishou.coupon.client.RecyclerCouponFeignClient;
import com.ilhaha.yueyishou.model.constant.CouponConstant;
import com.ilhaha.yueyishou.model.entity.order.OrderBill;
import com.ilhaha.yueyishou.model.enums.OrderStatus;
import com.ilhaha.yueyishou.model.form.coupon.UseCouponFrom;
import com.ilhaha.yueyishou.model.form.order.GenerateBillForm;
import com.ilhaha.yueyishou.order.mapper.OrderBillMapper;
import com.ilhaha.yueyishou.order.service.IOrderBillService;
import com.ilhaha.yueyishou.order.service.IOrderInfoService;
import io.seata.spring.annotation.GlobalTransactional;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Date;

@Service
public class OrderBillServiceImpl extends ServiceImpl<OrderBillMapper, OrderBill> implements IOrderBillService {


    @Resource
    private RecyclerCouponFeignClient recyclerCouponFeignClient;

    @Resource
    private IOrderInfoService orderInfoService;

    /**
     * 生成订单账单
     * @param generateBillForm
     * @return
     */
    @GlobalTransactional
    @Override
    public Boolean generateOrder(GenerateBillForm generateBillForm) {
        OrderBill orderBill = new OrderBill();
        BeanUtils.copyProperties(generateBillForm,orderBill);
        // 如果使用的服务抵扣劵，那么更新服务抵扣劵的状态
        if (ObjectUtils.isEmpty(generateBillForm.getCouponId())) {
            UseCouponFrom useCouponFrom = new UseCouponFrom();
            useCouponFrom.setCouponId(generateBillForm.getCouponId());
            useCouponFrom.setOrderId(generateBillForm.getOrderId());
            useCouponFrom.setRecyclerId(generateBillForm.getRecyclerId());
            useCouponFrom.setUsedTime(new Date());
            useCouponFrom.setStatus(CouponConstant.USED_STATUS);
            recyclerCouponFeignClient.useCoupon(useCouponFrom);
        }
        // 更新订单状态
        orderInfoService.updateOrderStatus(generateBillForm.getOrderId(), OrderStatus.CUSTOMER_CONFIRM_ORDER.getStatus());
        return this.save(orderBill);
    }
}
