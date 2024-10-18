package com.ilhaha.yueyishou.order.service.impl;

import com.alibaba.fastjson2.util.DateUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ilhaha.yueyishou.client.MapFeignClient;
import com.ilhaha.yueyishou.model.constant.PublicConstant;
import com.ilhaha.yueyishou.model.constant.RedisConstant;
import com.ilhaha.yueyishou.common.util.OrderUtil;
import com.ilhaha.yueyishou.model.constant.OrderConstant;
import com.ilhaha.yueyishou.model.entity.order.OrderInfo;
import com.ilhaha.yueyishou.model.enums.OrderStatus;
import com.ilhaha.yueyishou.model.form.map.CalculateLineForm;
import com.ilhaha.yueyishou.model.form.order.MatchingOrderForm;
import com.ilhaha.yueyishou.model.form.order.OrderMgrQueryForm;
import com.ilhaha.yueyishou.model.form.order.UpdateOrderFrom;
import com.ilhaha.yueyishou.model.vo.map.DrivingLineVo;
import com.ilhaha.yueyishou.model.vo.order.*;
import com.ilhaha.yueyishou.order.mapper.OrderInfoMapper;
import com.ilhaha.yueyishou.order.service.IOrderInfoService;
import com.ilhaha.yueyishou.recycler.client.RecyclerInfoFeignClient;
import jakarta.annotation.Resource;
import net.sf.jsqlparser.statement.select.Offset;
import org.springframework.beans.BeanUtils;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderInfoServiceImpl extends ServiceImpl<OrderInfoMapper, OrderInfo> implements IOrderInfoService {

    @Resource
    private OrderInfoMapper orderInfoMapper;

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private MapFeignClient mapFeignClient;


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
     * 顾客根据订单ID获取订单详情
     * @param orderId
     * @return
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
        // 删除redis中的订单
        redisTemplate.delete(RedisConstant.WAITING_ORDER + orderId);
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
                .filter(order -> !matchingOrderForm.getCustomerId().equals(order.getCustomerId()))
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
     * 回收员根据订单ID获取订单详情
     * @param recyclerId
     * @param orderId
     * @return
     */
    @Override
    public OrderDetailsVo getOrderDetails(Long recyclerId, Long orderId) {
        OrderDetailsVo orderDetailsVo = new OrderDetailsVo();
        OrderInfo orderInfoDB = this.getById(orderId);
        BeanUtils.copyProperties(orderInfoDB, orderDetailsVo);
        if (!ObjectUtils.isEmpty(orderInfoDB.getActualPhotos())) {
            String[] actualPhotoArr = orderInfoDB.getActualPhotos().split(",");
            orderDetailsVo.setActualPhoto(actualPhotoArr[0]);
            orderDetailsVo.setActualPhotoList(Arrays.stream(actualPhotoArr).toList());
        }
        // 查询redis查看该订单距离回收员多远
        // 获取回收员的位置
        Point recyclerLocation = getRecyclerLocationFromRedis(recyclerId);
        if (recyclerLocation != null && orderInfoDB.getCustomerPointLongitude() != null && orderInfoDB.getCustomerPointLatitude() != null) {
            // 订单中的客户位置
            Point customerLocation = new Point(orderInfoDB.getCustomerPointLongitude().doubleValue(),
                    orderInfoDB.getCustomerPointLatitude().doubleValue());

            // 使用 Haversine 公式计算距离（单位：公里）
            double distance = calculateDistance(recyclerLocation, customerLocation);

            // 将计算出的距离存储到 apart 字段
            orderDetailsVo.setApart(BigDecimal.valueOf(distance).setScale(1, RoundingMode.CEILING));

        }
        if (!ObjectUtils.isEmpty(orderDetailsVo.getArriveTime())) {
            // 计算回收员是否超时，并计算超时多少分钟
            Integer timoutMin = calculateTimeoutMinutes(orderDetailsVo.getAppointmentTime(),orderDetailsVo.getArriveTime());
            orderDetailsVo.setArriveTimoutMin(timoutMin);
        }
        return orderDetailsVo;
    }

    /**
     * 取消预约时间超时的订单
     * @return
     */
    @Override
    public Boolean processTimeoutOrders(List<Long> timeoutOrderIds) {
        LambdaUpdateWrapper<OrderInfo> orderInfoLambdaUpdateChainWrapper = new LambdaUpdateWrapper<>();
        orderInfoLambdaUpdateChainWrapper.set(OrderInfo::getStatus,OrderStatus.CANCELED_ORDER)
                .set(OrderInfo::getCancelMessage,OrderConstant.TIMEOUT_CANCEL_REMARK)
                .in(OrderInfo::getId,timeoutOrderIds);
        return this.update(orderInfoLambdaUpdateChainWrapper);
    }

    /**
     * 根据状态获取回收员订单列表
     * @param recyclerId
     * @param status
     * @return
     */
    @Override
    public List<RecyclerOrderVo> getRecyclerOrderListByStatus(Long recyclerId, Integer status) {
        List<RecyclerOrderVo> result = new ArrayList<>();
        LambdaQueryWrapper<OrderInfo> orderInfoLambdaQueryWrapper = new LambdaQueryWrapper<>();
        orderInfoLambdaQueryWrapper.eq(OrderInfo::getRecyclerId,recyclerId)
                .eq(OrderInfo::getStatus,status);
        List<OrderInfo> orderInfoListDB = this.list(orderInfoLambdaQueryWrapper);
        if (ObjectUtils.isEmpty(orderInfoListDB)) {
            return result;
        }
        Point recyclerLocationFromRedis = getRecyclerLocationFromRedis(recyclerId);
        result = orderInfoListDB.stream().map(order -> {
            RecyclerOrderVo recyclerOrderVo = new RecyclerOrderVo();
            BeanUtils.copyProperties(order,recyclerOrderVo);
            recyclerOrderVo.setActualPhoto(order.getActualPhotos().split(",")[0]);
            if (OrderStatus.RECYCLER_ACCEPTED.getStatus().equals(status) && !ObjectUtils.isEmpty(recyclerLocationFromRedis)) {
                // 计算回收员距离订单多远
                CalculateLineForm calculateLineForm = new CalculateLineForm();
                calculateLineForm.setRecyclerId(recyclerId);
                calculateLineForm.setEndPointLatitude(order.getCustomerPointLatitude());
                calculateLineForm.setEndPointLongitude(order.getCustomerPointLongitude());
                DrivingLineVo drivingLineVo = mapFeignClient.calculateDrivingLine(calculateLineForm).getData();
                recyclerOrderVo.setApart(drivingLineVo.getDistance());
            }
            if (!ObjectUtils.isEmpty(recyclerOrderVo.getArriveTime())) {
                // 计算回收员是否超时，并计算超时多少分钟
                Integer timoutMin = calculateTimeoutMinutes(recyclerOrderVo.getAppointmentTime(),recyclerOrderVo.getArriveTime());
                recyclerOrderVo.setArriveTimoutMin(timoutMin);
            }
            return recyclerOrderVo;
        }).collect(Collectors.toList());
        return result;
    }

    /**
     * 计算回收员是否超时，并返回超时分钟数
     * @param appointmentTime 预约时间，格式为 yyyy-MM-dd HH:mm:ss
     * @param arriveTime 到达时间，格式为 yyyy-MM-dd HH:mm:ss
     * @return 超时的分钟数，未超时返回 0
     */
    private static int calculateTimeoutMinutes(Date appointmentTime, Date arriveTime) {
        // 获取两个时间的毫秒值
        long appointmentMillis = appointmentTime.getTime();
        long arriveMillis = arriveTime.getTime();

        // 计算时间差（毫秒级）
        long differenceInMillis = arriveMillis - appointmentMillis;

        // 如果到达时间早于或等于预约时间，则未超时，返回 0
        if (differenceInMillis <= 0) {
            return 0;
        }

        // 将毫秒差转换为分钟
        return (int) (differenceInMillis / (1000 * 60));
    }


    /**
     * 回收员接单后，在预约时间前一个小时取消订单时，要重新把订单给别的回收员接单
     * @param orderId
     * @return
     */
    @Override
    public Boolean repost(Long orderId) {
        // 更新订单信息
        LambdaUpdateWrapper<OrderInfo> orderInfoLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        orderInfoLambdaUpdateWrapper.set(OrderInfo::getRecyclerId,null)
                .set(OrderInfo::getStatus,OrderStatus.WAITING_ACCEPT)
                .set(OrderInfo::getAcceptTime,null)
                .eq(OrderInfo::getId,orderId);
        Boolean flag = this.update(orderInfoLambdaUpdateWrapper);
        if (flag) {
            // 重新查出订单信息
            OrderInfo orderInfoDB = this.getById(orderId);
            // 重新存入redis
            redisTemplate.opsForValue().set(RedisConstant.WAITING_ORDER + orderId,orderInfoDB);
        }
        return flag;
    }

    /**
     * 回收员到达回收点
     * @param orderId
     * @return
     */
    @Override
    public Boolean arrive(Long orderId) {
        //TODO 之后要完善不能超过100米就点击已到达
        LambdaUpdateWrapper<OrderInfo> orderInfoLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        orderInfoLambdaUpdateWrapper.set(OrderInfo::getStatus,OrderStatus.RECYCLER_CONFIRM_ORDER)
                .set(OrderInfo::getArriveTime,new Date())
                .eq(OrderInfo::getId,orderId);
        return this.update(orderInfoLambdaUpdateWrapper);
    }

    /**
     * 更新订单信息
     * @param updateOrderFrom
     * @return
     */
    @Override
    public Boolean updateOrder(UpdateOrderFrom updateOrderFrom) {
        OrderInfo orderInfo = new OrderInfo();
        BeanUtils.copyProperties(updateOrderFrom,orderInfo);
        return this.updateById(orderInfo);
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
            if (ObjectUtils.isEmpty(location)) {
                return null;
            }
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
