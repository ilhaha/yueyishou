package com.ilhaha.yueyishou.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ilhaha.yueyishou.model.constant.PublicConstant;
import com.ilhaha.yueyishou.model.constant.RedisConstant;
import com.ilhaha.yueyishou.common.util.OrderUtil;
import com.ilhaha.yueyishou.model.constant.OrderConstant;
import com.ilhaha.yueyishou.model.entity.order.OrderInfo;
import com.ilhaha.yueyishou.model.enums.OrderStatus;
import com.ilhaha.yueyishou.model.form.order.MatchingOrderForm;
import com.ilhaha.yueyishou.model.form.order.OrderMgrQueryForm;
import com.ilhaha.yueyishou.model.vo.order.*;
import com.ilhaha.yueyishou.order.mapper.OrderInfoMapper;
import com.ilhaha.yueyishou.order.service.IOrderInfoService;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderInfoServiceImpl extends ServiceImpl<OrderInfoMapper, OrderInfo> implements IOrderInfoService {

    @Resource
    private OrderInfoMapper orderInfoMapper;

    @Resource
    private RedisTemplate redisTemplate;


    /**
     * 订单分页列表查询
     *
     * @param orderMgrQueryForm
     * @param page
     * @return
     */
    @Override
    public Page<OrderMgrQueryVo> queryPageList(OrderMgrQueryForm orderMgrQueryForm, Page<OrderMgrQueryVo> page) {
        return orderInfoMapper.queryPageList(page, orderMgrQueryForm);
    }

    /**
     * 下单
     *
     * @param placeOrderForm
     * @return
     */
    @Override
    public Boolean placeOrder(PlaceOrderForm placeOrderForm) {
        OrderInfo orderInfo = new OrderInfo();
        BeanUtils.copyProperties(placeOrderForm, orderInfo);
        orderInfo.setOrderNo(OrderUtil.generateOrderNumber());
        boolean flag = this.save(orderInfo);
        // 将新订单存储到redis中
        if (flag) {
            redisTemplate.opsForValue().set(RedisConstant.WAITING_ORDER + orderInfo.getId(),orderInfo);
        }
        return flag;
    }

    /**
     * 根据订单状态获取订单列表
     *
     * @param status
     * @return
     */
    @Override
    public List<CustomerOrderListVo> orderList(Integer status, Long customerId) {
        LambdaUpdateWrapper<OrderInfo> orderInfoLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        orderInfoLambdaUpdateWrapper.eq(OrderInfo::getStatus, status)
                .eq(OrderInfo::getCustomerId, customerId);
        List<OrderInfo> listDB = this.list(orderInfoLambdaUpdateWrapper);
        return listDB.stream().map(item -> {
            CustomerOrderListVo customerOrderListVo = new CustomerOrderListVo();
            BeanUtils.copyProperties(item, customerOrderListVo);
            customerOrderListVo.setActualPhoto(item.getActualPhotos().split(",")[0]);
            return customerOrderListVo;
        }).collect(Collectors.toList());
    }

    /**
     * 根据订单ID获取订单详情
     */
    @Override
    public OrderDetailsVo getOrderDetails(Long orderId) {
        OrderInfo orderInfoDB = this.getById(orderId);
        OrderDetailsVo customerOrderDetailsVo = new OrderDetailsVo();
        BeanUtils.copyProperties(orderInfoDB, customerOrderDetailsVo);
        if (!ObjectUtils.isEmpty(orderInfoDB.getActualPhotos())) {
            String[] actualPhotoArr = orderInfoDB.getActualPhotos().split(",");
            customerOrderDetailsVo.setActualPhoto(actualPhotoArr[0]);
            customerOrderDetailsVo.setActualPhotoList(Arrays.stream(actualPhotoArr).toList());
        }
        return customerOrderDetailsVo;
    }

    /**
     * 取消订单
     *
     * @param orderId
     * @return
     */
    @Override
    public Boolean cancelOrder(Long orderId) {
        LambdaUpdateWrapper<OrderInfo> orderInfoLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        orderInfoLambdaUpdateWrapper.set(OrderInfo::getStatus, OrderStatus.CANCELED_ORDER)
                .set(OrderInfo::getCancelMessage, OrderConstant.CANCEL_REMARK)
                .eq(OrderInfo::getId, orderId);
        return this.update(orderInfoLambdaUpdateWrapper);
    }

    /**
     * 回收员获取符合接单的订单
     *
     * @param matchingOrderForm
     * @return
     */
    @Override
    public List<MatchingOrderVo> retrieveMatchingOrders(MatchingOrderForm matchingOrderForm) {
        ArrayList<MatchingOrderVo> result = new ArrayList<>();
        // 查询redis所有的待接单的订单信息
        Set keys = redisTemplate.keys(RedisConstant.SELECT_WAITING_ORDER);

        // 使用 multiGet 批量获取这些键对应的 OrderInfo 对象
        List<OrderInfo> waitOrderInfoList = redisTemplate.opsForValue().multiGet(keys);

        // 过滤掉是自己的订单，自己不能接自己的订单
        List<OrderInfo> filteredOrders = waitOrderInfoList.stream()
                .filter(order -> !order.getCustomerId().equals(matchingOrderForm.getCustomerId()))
                .collect(Collectors.toList());

        // 过滤出未过预约时间的订单
        List<OrderInfo> validOrders = filterValidOrdersByAppointmentTime(filteredOrders);

        // 过滤出符合回收员回收类型的订单
        List<OrderInfo> filteredByTypeOrders = filterOrdersByRecyclingType(validOrders, matchingOrderForm.getRecyclingType());

        // 过滤出符合接单里程的订单并将距离保存到 MatchingOrderVo 的 apart 字段
        filterOrdersByDistance(filteredByTypeOrders, getRecyclerLocationFromRedis(matchingOrderForm.getRecyclerId()), matchingOrderForm.getAcceptDistance(), result);

        // 返回结果
        return result;
    }

    /**
     * 回收员抢单
     * @param recyclerId
     * @param orderId
     * @return
     */
    @Override
    public Boolean grabOrder(Long recyclerId, Long orderId) {
        String waitOrderKey = RedisConstant.WAITING_ORDER + orderId;
        Boolean hasOrder = redisTemplate.hasKey(waitOrderKey);
        // 存在key证明订单还未被抢
        if (hasOrder) {
            // 删除key
            redisTemplate.delete(waitOrderKey);
            // 更新订单信息
            OrderInfo orderInfo = new OrderInfo();
            orderInfo.setId(orderId);
            orderInfo.setRecyclerId(recyclerId);
            orderInfo.setStatus(OrderStatus.RECYCLER_ACCEPTED.getStatus());
            orderInfo.setAcceptTime(new Date());
            this.updateById(orderInfo);
        }
        return hasOrder;
    }

    /**
     * 过滤出未过预约时间的订单
     *
     * @param orders 原始订单列表
     * @return 未过预约时间的订单列表
     */
    private List<OrderInfo> filterValidOrdersByAppointmentTime(List<OrderInfo> orders) {
        Date now = new Date(); // 获取当前时间
        return orders.stream()
                .filter(order -> order.getAppointmentTime().after(now))  // 过滤掉预约时间已过的订单
                .collect(Collectors.toList());  // 返回过滤后的订单列表
    }

    /**
     * 根据回收员回收的类型过滤订单
     *
     * @param orders        已经过滤了预约时间的订单
     * @param recyclingType 回收员回收类型 (逗号分隔的字符串)
     * @return 符合回收类型的订单列表
     */
    private List<OrderInfo> filterOrdersByRecyclingType(List<OrderInfo> orders, String recyclingType) {
        if (recyclingType == null || recyclingType.isEmpty()) {
            return orders;  // 如果回收类型为空，返回所有订单
        }

        // 将回收类型的字符串按逗号分割成列表
        List<String> recyclingTypes = Arrays.asList(recyclingType.split(","));

        return orders.stream()
                .filter(order -> recyclingTypes.contains(order.getParentCategoryId().toString()))  // 假设 parentCategoryId 是数字类型
                .collect(Collectors.toList());
    }


    /**
     * 从 Redis 获取回收员的实时位置
     *
     * @param recyclerId 回收员ID
     * @return 回收员的地理位置 (Point)
     */
    private Point getRecyclerLocationFromRedis(Long recyclerId) {
        List<Point> recyclerLocations = redisTemplate.opsForGeo()
                .position(RedisConstant.RECYCLER_GEO_LOCATION, recyclerId.toString());

        if (recyclerLocations != null && !recyclerLocations.isEmpty()) {
            Point location = recyclerLocations.get(0);  // 获取第一个位置点
            return new Point(location.getX(), location.getY());  // 返回经纬度
        }

        return null;  // 如果没有位置数据，则返回空
    }

    /**
     * 过滤出符合接单里程的订单，并将结果添加到 result 中
     *
     * @param orders           订单列表
     * @param recyclerLocation 回收员当前的地理位置
     * @param acceptDistance   回收员能接受的最大里程
     * @param result           用于保存最终的 MatchingOrderVo 结果
     */
    private void filterOrdersByDistance(List<OrderInfo> orders, Point recyclerLocation, BigDecimal acceptDistance, List<MatchingOrderVo> result) {
        if (ObjectUtils.isEmpty(recyclerLocation)) {
            return;
        }
        orders.stream()
                .filter(order -> {
                    // 计算订单的客户位置与回收员位置之间的距离
                    Point customerLocation = new Point(order.getCustomerPointLongitude().doubleValue(), order.getCustomerPointLatitude().doubleValue());
                    double distance = calculateDistance(recyclerLocation, customerLocation);  // 使用 Haversine 公式计算距离

                    // 过滤掉超过回收员可接受距离的订单
                    if (distance <= acceptDistance.doubleValue()) {
                        // 创建 MatchingOrderVo 并将距离信息保存到 apart 字段
                        MatchingOrderVo vo = new MatchingOrderVo();
                        BeanUtils.copyProperties(order, vo);  // 拷贝订单的其他信息
                        if (!ObjectUtils.isEmpty(order.getActualPhotos())) {
                            vo.setActualPhoto(order.getActualPhotos().split(",")[0]);
                        }
                        // 将距离向上取整并保存到 apart 字段
                        vo.setApart(BigDecimal.valueOf(distance).setScale(1, RoundingMode.CEILING));
                        result.add(vo);  // 添加到 result 列表
                        return true;
                    }
                    return false;
                })
                .collect(Collectors.toList());
    }

    /**
     * 使用 Haversine 公式计算两个经纬度之间的距离
     *
     * @param point1 第一个位置 (经纬度)
     * @param point2 第二个位置 (经纬度)
     * @return 距离，单位为公里
     */
    private double calculateDistance(Point point1, Point point2) {

        double lat1 = Math.toRadians(point1.getY());
        double lon1 = Math.toRadians(point1.getX());
        double lat2 = Math.toRadians(point2.getY());
        double lon2 = Math.toRadians(point2.getX());

        double dlat = lat2 - lat1;
        double dlon = lon2 - lon1;

        double a = Math.sin(dlat / 2) * Math.sin(dlat / 2) +
                Math.cos(lat1) * Math.cos(lat2) *
                        Math.sin(dlon / 2) * Math.sin(dlon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return PublicConstant.EARTH_RADIUS * c;  // 返回距离，单位：公里
    }
}
