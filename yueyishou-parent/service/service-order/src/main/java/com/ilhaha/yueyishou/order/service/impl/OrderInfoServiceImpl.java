package com.ilhaha.yueyishou.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ilhaha.yueyishou.common.result.Result;
import com.ilhaha.yueyishou.common.util.OrderUtil;
import com.ilhaha.yueyishou.model.constant.OrderConstant;
import com.ilhaha.yueyishou.model.entity.customer.CustomerInfo;
import com.ilhaha.yueyishou.model.entity.order.OrderInfo;
import com.ilhaha.yueyishou.model.enums.OrderStatus;
import com.ilhaha.yueyishou.model.form.order.ServiceFeeRuleRequestForm;
import com.ilhaha.yueyishou.model.form.order.OrderMgrQueryForm;
import com.ilhaha.yueyishou.model.vo.order.*;
import com.ilhaha.yueyishou.order.mapper.OrderInfoMapper;
import com.ilhaha.yueyishou.order.service.IOrderInfoService;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderInfoServiceImpl extends ServiceImpl<OrderInfoMapper, OrderInfo> implements IOrderInfoService {

    @Resource
    private OrderInfoMapper orderInfoMapper;



    /**
     * 订单分页列表查询
     *
     * @param orderMgrQueryForm
     * @param page
     * @return
     */
    @Override
    public Page<OrderMgrQueryVo> queryPageList(OrderMgrQueryForm orderMgrQueryForm, Page<OrderMgrQueryVo> page) {
        return orderInfoMapper.queryPageList(page,orderMgrQueryForm);
    }

    /**
     * 下单
     * @param placeOrderForm
     * @return
     */
    @Override
    public Boolean placeOrder(PlaceOrderForm placeOrderForm) {
        OrderInfo orderInfo = new OrderInfo();
        BeanUtils.copyProperties(placeOrderForm,orderInfo);
        orderInfo.setOrderNo(OrderUtil.generateOrderNumber());
        return this.save(orderInfo);
    }

    /**
     * 根据订单状态获取订单列表
     * @param status
     * @return
     */
    @Override
    public List<CustomerOrderListVo> orderList(Integer status, Long customerId) {
        LambdaUpdateWrapper<OrderInfo> orderInfoLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        orderInfoLambdaUpdateWrapper.eq(OrderInfo::getStatus,status)
                .eq(OrderInfo::getCustomerId,customerId);
        List<OrderInfo> listDB = this.list(orderInfoLambdaUpdateWrapper);
        return listDB.stream().map(item -> {
            CustomerOrderListVo customerOrderListVo = new CustomerOrderListVo();
            BeanUtils.copyProperties(item,customerOrderListVo);
            customerOrderListVo.setActualPhoto(item.getActualPhotos().split(",")[0]);
            return customerOrderListVo;
        }).collect(Collectors.toList());
    }

    /**
     * 根据订单ID获取订单详情
     */
    @Override
    public CustomerOrderDetailsVo getOrderDetails(Long orderId) {
        OrderInfo orderInfoDB = this.getById(orderId);
        CustomerOrderDetailsVo customerOrderDetailsVo = new CustomerOrderDetailsVo();
        BeanUtils.copyProperties(orderInfoDB,customerOrderDetailsVo);
        if (!ObjectUtils.isEmpty(orderInfoDB.getActualPhotos())) {
            String[] actualPhotoArr = orderInfoDB.getActualPhotos().split(",");
            customerOrderDetailsVo.setActualPhoto(actualPhotoArr[0]);
            customerOrderDetailsVo.setActualPhotoList(Arrays.stream(actualPhotoArr).toList());
        }
        return customerOrderDetailsVo;
    }

    /**
     * 取消订单
     * @param orderId
     * @return
     */
    @Override
    public Boolean cancelOrder(Long orderId) {
        LambdaUpdateWrapper<OrderInfo> orderInfoLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        orderInfoLambdaUpdateWrapper.set(OrderInfo::getStatus, OrderStatus.CANCELED_ORDER)
                .set(OrderInfo::getCancelMessage, OrderConstant.CANCEL_REMARK)
                .eq(OrderInfo::getId,orderId);
        return this.update(orderInfoLambdaUpdateWrapper);
    }
}
