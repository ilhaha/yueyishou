package com.ilhaha.yueyishou.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ilhaha.yueyishou.common.util.RecycleCodeGenerator;
import com.ilhaha.yueyishou.coupon.client.CustomerCouponFeignClient;
import com.ilhaha.yueyishou.coupon.client.RecyclerCouponFeignClient;
import com.ilhaha.yueyishou.model.constant.CouponConstant;
import com.ilhaha.yueyishou.model.constant.RedisConstant;
import com.ilhaha.yueyishou.model.entity.order.OrderBill;
import com.ilhaha.yueyishou.model.enums.OrderStatus;
import com.ilhaha.yueyishou.model.form.coupon.UseCouponFrom;
import com.ilhaha.yueyishou.model.form.order.GenerateBillForm;
import com.ilhaha.yueyishou.model.form.order.UpdateBillForm;
import com.ilhaha.yueyishou.order.mapper.OrderBillMapper;
import com.ilhaha.yueyishou.order.service.IOrderBillService;
import com.ilhaha.yueyishou.order.service.IOrderInfoService;
import io.seata.spring.annotation.GlobalTransactional;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Date;
import java.util.List;

@Service
public class OrderBillServiceImpl extends ServiceImpl<OrderBillMapper, OrderBill> implements IOrderBillService {


    @Resource
    private RecyclerCouponFeignClient recyclerCouponFeignClient;

    @Resource
    private CustomerCouponFeignClient customerCouponFeignClient;

    @Resource
    private IOrderInfoService orderInfoService;

    @Resource
    private RedisTemplate redisTemplate;

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
        if (!ObjectUtils.isEmpty(generateBillForm.getCouponId())) {
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

    /**
     * 根据订单id获取订单账单信息
     * @param orderId
     * @return
     */
    @Override
    public OrderBill getBillInfoByOrderId(Long orderId) {
        LambdaQueryWrapper<OrderBill> orderBillLambdaQueryWrapper = new LambdaQueryWrapper<>();
        orderBillLambdaQueryWrapper.eq(OrderBill::getOrderId,orderId);
        return this.getOne(orderBillLambdaQueryWrapper);
    }

    /**
     * 修改账单信息
     * @param updateBillForm
     * @return
     */
    @GlobalTransactional
    @Override
    public String updateBill(UpdateBillForm updateBillForm) {
        LambdaUpdateWrapper<OrderBill> orderBillLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        orderBillLambdaUpdateWrapper.eq(OrderBill::getOrderId,updateBillForm.getOrderId())
                .set(!ObjectUtils.isEmpty(updateBillForm.getCustomerCouponAmount()),OrderBill::getCustomerCouponAmount,updateBillForm.getCustomerCouponAmount())
                .set(!ObjectUtils.isEmpty(updateBillForm.getRealCustomerPlatformAmount()),OrderBill::getRealCustomerPlatformAmount,updateBillForm.getRealCustomerPlatformAmount())
                .set(!ObjectUtils.isEmpty(updateBillForm.getRealCustomerRecycleAmount()),OrderBill::getRealCustomerRecycleAmount,updateBillForm.getRealCustomerRecycleAmount())
                .set(OrderBill::getUpdateTime,new Date())
                .set(!ObjectUtils.isEmpty(updateBillForm.getPayTime()),OrderBill::getPayTime,updateBillForm.getPayTime())
                .set(!ObjectUtils.isEmpty(updateBillForm.getTransactionId()),OrderBill::getTransactionId,updateBillForm.getTransactionId());
        this.update(orderBillLambdaUpdateWrapper);
        // 如果顾客使用的服务抵扣劵，则修改服务抵扣劵信息
        if (!ObjectUtils.isEmpty(updateBillForm.getCouponId())) {
            UseCouponFrom useCouponFrom = new UseCouponFrom();
            useCouponFrom.setCouponId(updateBillForm.getCouponId());
            useCouponFrom.setOrderId(updateBillForm.getOrderId());
            useCouponFrom.setCustomerId(updateBillForm.getCustomerId());
            useCouponFrom.setUsedTime(new Date());
            useCouponFrom.setStatus(CouponConstant.USED_STATUS);
            customerCouponFeignClient.useCoupon(useCouponFrom);
        }
        String recycleCode = RecycleCodeGenerator.generateUniqueSixDigitNumber();
        redisTemplate.opsForValue().set(RedisConstant.RECYCLE_CODE + updateBillForm.getOrderId(),recycleCode);
        return recycleCode;
    }

    /**
     * 批量获取订单账单信息
     * @param orderIds
     * @return
     */
    @Override
    public List<OrderBill> getBillInfoByOrderIds(List<Long> orderIds) {
        LambdaQueryWrapper<OrderBill> orderBillLambdaQueryWrapper = new LambdaQueryWrapper<>();
        orderBillLambdaQueryWrapper.in(OrderBill::getOrderId,orderIds);
        return this.list(orderBillLambdaQueryWrapper);
    }
}
