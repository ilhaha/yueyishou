package com.ilhaha.yueyishou.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ilhaha.yueyishou.client.MapFeignClient;
import com.ilhaha.yueyishou.common.execption.YueYiShouException;
import com.ilhaha.yueyishou.common.result.ResultCodeEnum;
import com.ilhaha.yueyishou.common.util.BillUtils;
import com.ilhaha.yueyishou.coupon.client.CustomerCouponFeignClient;
import com.ilhaha.yueyishou.coupon.client.RecyclerCouponFeignClient;
import com.ilhaha.yueyishou.customer.client.CustomerAccountFeignClient;
import com.ilhaha.yueyishou.model.constant.PublicConstant;
import com.ilhaha.yueyishou.model.constant.RedisConstant;
import com.ilhaha.yueyishou.common.util.OrderUtil;
import com.ilhaha.yueyishou.model.constant.OrderConstant;
import com.ilhaha.yueyishou.model.entity.order.OrderBill;
import com.ilhaha.yueyishou.model.entity.order.OrderInfo;
import com.ilhaha.yueyishou.model.enums.OrderStatus;
import com.ilhaha.yueyishou.model.form.coupon.AvailableCouponForm;
import com.ilhaha.yueyishou.model.form.customer.CustomerWithdrawForm;
import com.ilhaha.yueyishou.model.form.map.CalculateLineForm;
import com.ilhaha.yueyishou.model.form.order.*;
import com.ilhaha.yueyishou.model.form.recycler.RecyclerAccountForm;
import com.ilhaha.yueyishou.model.form.recycler.RecyclerWithdrawForm;
import com.ilhaha.yueyishou.model.vo.coupon.AvailableCouponVo;
import com.ilhaha.yueyishou.model.vo.map.DrivingLineVo;
import com.ilhaha.yueyishou.model.vo.order.*;
import com.ilhaha.yueyishou.model.vo.recycler.RecyclerAccountVo;
import com.ilhaha.yueyishou.order.mapper.OrderInfoMapper;
import com.ilhaha.yueyishou.order.service.IOrderBillService;
import com.ilhaha.yueyishou.order.service.IOrderCommentService;
import com.ilhaha.yueyishou.order.service.IOrderInfoService;
import com.ilhaha.yueyishou.recycler.client.RecyclerAccountFeignClient;
import com.ilhaha.yueyishou.rules.client.ServiceFeeRuleFeignClient;
import io.seata.spring.annotation.GlobalTransactional;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
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

    @Resource
    private ServiceFeeRuleFeignClient serviceFeeRuleFeignClient;

    @Resource
    private RecyclerCouponFeignClient recyclerCouponFeignClient;

    @Resource
    private CustomerCouponFeignClient customerCouponFeignClient;

    @Resource
    @Lazy
    private IOrderBillService orderBillService;

    @Resource
    private RecyclerAccountFeignClient recyclerAccountFeignClient;

    @Resource
    private CustomerAccountFeignClient customerAccountFeignClient;

    @Resource
    @Lazy
    private IOrderCommentService orderCommentService;


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
        orderInfo.setStatus(OrderStatus.WAITING_ACCEPT.getStatus());
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
        LambdaQueryWrapper<OrderInfo> orderInfoLambdaQueryWrapper = new LambdaQueryWrapper<>();
        orderInfoLambdaQueryWrapper.eq(OrderInfo::getStatus, status)
                .eq(OrderInfo::getCustomerId, customerId);
        List<OrderInfo> listDB = this.list(orderInfoLambdaQueryWrapper);

        return listDB
                .stream()
                .sorted(Comparator.comparing(OrderInfo::getCreateTime).reversed())
                .map(item -> {
            CustomerOrderListVo customerOrderListVo = new CustomerOrderListVo();
            BeanUtils.copyProperties(item, customerOrderListVo);
            customerOrderListVo.setActualPhoto(item.getActualPhotos().split(",")[0]);
            // 查看订单账单信息
            OrderBill orderBillDB = orderBillService.getBillInfoByOrderId(item.getId());
            if (!ObjectUtils.isEmpty(orderBillDB)) {
                customerOrderListVo.setRealOrderRecycleAmount(orderBillDB.getRealOrderRecycleAmount());
                customerOrderListVo.setRealCustomerPlatformAmount(orderBillDB.getRealCustomerPlatformAmount());
                customerOrderListVo.setRecyclerOvertimeCharges(orderBillDB.getRecyclerOvertimeCharges());
                customerOrderListVo.setRealCustomerRecycleAmount(orderBillDB.getRealCustomerRecycleAmount());
                customerOrderListVo.setPayTime(orderBillDB.getPayTime());
            }
            // 查询回收员超时信息
            if (!ObjectUtils.isEmpty(item.getArriveTime())) {
                Integer timeOutMin = calculateTimeoutMinutes(item.getAppointmentTime(),item.getArriveTime());
                customerOrderListVo.setArriveTimoutMin(timeOutMin);
                // 超时计算超时费用
                if (timeOutMin > 0) {
                    OvertimeRequestForm overtimeRequestForm = new OvertimeRequestForm();
                    overtimeRequestForm.setOvertimeMinutes(timeOutMin);
                    OvertimeResponseVo overtimeResponseVo = serviceFeeRuleFeignClient.calculateTimeoutFree(overtimeRequestForm).getData();
                    customerOrderListVo.setRecyclerOvertimeCharges(overtimeResponseVo.getOvertimeFee());
                }
            }
            // 查询订单列表评论信息
            OrderCommentVo orderCommentVo = orderCommentService.getOrderComment(item.getId());
            if (!ObjectUtils.isEmpty(orderCommentVo.getReviewContent())) {
                customerOrderListVo.setReviewContent(orderCommentVo.getReviewContent());
                customerOrderListVo.setReviewTime(orderCommentVo.getReviewTime());
            }
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
        if (!ObjectUtils.isEmpty(customerOrderDetailsVo.getArriveTime())) {
            // 计算回收员是否超时，并计算超时多少分钟
            Integer timoutMin = calculateTimeoutMinutes(customerOrderDetailsVo.getAppointmentTime(),customerOrderDetailsVo.getArriveTime());
            customerOrderDetailsVo.setArriveTimoutMin(timoutMin);
            // 超时计算超时费用
            if (timoutMin > 0) {
                OvertimeRequestForm overtimeRequestForm = new OvertimeRequestForm();
                overtimeRequestForm.setOvertimeMinutes(timoutMin);
                OvertimeResponseVo overtimeResponseVo = serviceFeeRuleFeignClient.calculateTimeoutFree(overtimeRequestForm).getData();
                customerOrderDetailsVo.setRecyclerOvertimeCharges(overtimeResponseVo.getOvertimeFee());
            }
        }
        // 获取订单账单信息
        OrderBill orderBillDB = orderBillService.getBillInfoByOrderId(orderId);
        if (!ObjectUtils.isEmpty(orderBillDB)) {
            customerOrderDetailsVo.setRealOrderRecycleAmount(orderBillDB.getRealOrderRecycleAmount());
            customerOrderDetailsVo.setRealCustomerPlatformAmount(orderBillDB.getRealCustomerPlatformAmount());
            customerOrderDetailsVo.setRecyclerOvertimeCharges(orderBillDB.getRecyclerOvertimeCharges());
            customerOrderDetailsVo.setRealCustomerRecycleAmount(orderBillDB.getRealCustomerRecycleAmount());
            customerOrderDetailsVo.setPayTime(orderBillDB.getPayTime());
            // 顾客确认订单时，获取顾客可以使用服务抵扣劵
            if (OrderStatus.CUSTOMER_CONFIRM_ORDER.getStatus().equals(orderInfoDB.getStatus())) {
                AvailableCouponForm availableCouponForm = new AvailableCouponForm();
                availableCouponForm.setCustomerId(orderInfoDB.getCustomerId());
                availableCouponForm.setRealRecyclerAmount(orderBillDB.getRealOrderRecycleAmount());
                List<AvailableCouponVo> availableCouponVoList = customerCouponFeignClient.getAvailableCustomerServiceCoupons(availableCouponForm).getData();
                customerOrderDetailsVo.setAvailableCouponVoList(availableCouponVoList);
            }
        }
        // 查询订单列表评论信息
        OrderCommentVo orderCommentVo = orderCommentService.getOrderComment(orderId);
        if (!ObjectUtils.isEmpty(orderCommentVo.getReviewContent())) {
            customerOrderDetailsVo.setReviewContent(orderCommentVo.getReviewContent());
            customerOrderDetailsVo.setReviewTime(orderCommentVo.getReviewTime());
        }

        // 从redis中判断查询有没有该订单的回收码
        String recycleCode = (String) redisTemplate.opsForValue().get(RedisConstant.RECYCLE_CODE + orderId);
        customerOrderDetailsVo.setRecycleCode(recycleCode);
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
                .set(OrderInfo::getUpdateTime,new Date())
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
            if (timoutMin > 0) {
                OvertimeRequestForm overtimeRequestForm = new OvertimeRequestForm();
                overtimeRequestForm.setOvertimeMinutes(timoutMin);
                OvertimeResponseVo overtimeResponseVo = serviceFeeRuleFeignClient.calculateTimeoutFree(overtimeRequestForm).getData();
                orderDetailsVo.setRecyclerOvertimeCharges(overtimeResponseVo.getOvertimeFee());
            }

        }
        // 查看订单是否有账单信息
        OrderBill orderBillDB = orderBillService.getBillInfoByOrderId(orderId);
        if (!ObjectUtils.isEmpty(orderBillDB)) {
            orderDetailsVo.setRealOrderRecycleAmount(orderBillDB.getRealOrderRecycleAmount());
            orderDetailsVo.setRealRecyclerPlatformAmount(orderBillDB.getRealRecyclerPlatformAmount());
            orderDetailsVo.setRealRecyclerAmount(orderBillDB.getRealRecyclerAmount());
            orderDetailsVo.setPayTime(orderBillDB.getPayTime());
        }

        // 查询订单列表评论信息
        OrderCommentVo orderCommentVo = orderCommentService.getOrderComment(orderId);
        if (!ObjectUtils.isEmpty(orderCommentVo.getReviewContent())) {
            orderDetailsVo.setReviewContent(orderCommentVo.getReviewContent());
            orderDetailsVo.setReviewTime(orderCommentVo.getReviewTime());
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
                if (timoutMin > 0) {
                    OvertimeRequestForm overtimeRequestForm = new OvertimeRequestForm();
                    overtimeRequestForm.setOvertimeMinutes(timoutMin);
                    OvertimeResponseVo overtimeResponseVo = serviceFeeRuleFeignClient.calculateTimeoutFree(overtimeRequestForm).getData();
                    recyclerOrderVo.setRecyclerOvertimeCharges(overtimeResponseVo.getOvertimeFee());
                }
            }

            // 查询订单账单信息
            OrderBill orderBillDB = orderBillService.getBillInfoByOrderId(order.getId());
            if (!ObjectUtils.isEmpty(orderBillDB)) {
                recyclerOrderVo.setRealOrderRecycleAmount(orderBillDB.getRealOrderRecycleAmount());
                recyclerOrderVo.setRealRecyclerPlatformAmount(orderBillDB.getRealRecyclerPlatformAmount());
                recyclerOrderVo.setRecyclerOvertimeCharges(orderBillDB.getRecyclerOvertimeCharges());
                recyclerOrderVo.setPayTime(orderBillDB.getPayTime());
                recyclerOrderVo.setRealRecyclerAmount(orderBillDB.getRealRecyclerAmount());
            }


            // 查询订单列表评论信息
            OrderCommentVo orderCommentVo = orderCommentService.getOrderComment(order.getId());
            if (!ObjectUtils.isEmpty(orderCommentVo.getReviewContent())) {
                recyclerOrderVo.setReviewContent(orderCommentVo.getReviewContent());
                recyclerOrderVo.setReviewTime(orderCommentVo.getReviewTime());
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
        orderInfo.setUpdateTime(new Date());
        return this.updateById(orderInfo);
    }

    /**
     * 计算实际的订单信息
     * @param orderId
     * @return
     */
    @Override
    public CalculateActualOrderVo calculateActual(Long orderId) {
        CalculateActualOrderVo calculateActualOrderVo = new CalculateActualOrderVo();
        // 计算回收员总共所需回收支出金额
        BigDecimal totalAmount = new BigDecimal(BigInteger.ZERO);

        OrderInfo orderInfoDB = this.getById(orderId);
        calculateActualOrderVo.setOrderId(orderInfoDB.getId());

        // 计算订单实际回收总金额、回收员实际所付款手续费、顾客实际所付款手续费
        ServiceFeeRuleRequestForm serviceFeeRuleRequestForm = new ServiceFeeRuleRequestForm();
        serviceFeeRuleRequestForm.setRecycleWeigh(orderInfoDB.getRecycleWeigh());
        serviceFeeRuleRequestForm.setUnitPrice(orderInfoDB.getUnitPrice());
        calculateOrderAndFees(calculateActualOrderVo,serviceFeeRuleRequestForm);
        totalAmount = totalAmount.add(calculateActualOrderVo.getRealRecyclerAmount()).add(calculateActualOrderVo.getRealRecyclerPlatformAmount());

        // 判断回收员是否超时
        int timeOutMin = calculateTimeoutMinutes(orderInfoDB.getAppointmentTime(), orderInfoDB.getArriveTime());
        // 已超时
        if (OrderConstant.NOT_TIMED_OUT_MIN < timeOutMin) {
            // 计算回收员需要付的超时费用
            calculateTimeoutFree(calculateActualOrderVo,timeOutMin);
            calculateActualOrderVo.setTimeOutMin(timeOutMin);
            totalAmount = totalAmount.add(calculateActualOrderVo.getRecyclerOvertimeCharges());
        }
        // 获取回收员可使用的服务抵扣劵
        AvailableCouponForm availableCouponForm = new AvailableCouponForm();
        availableCouponForm.setRealRecyclerAmount(calculateActualOrderVo.getRealRecyclerAmount());
        availableCouponForm.setRecyclerId(orderInfoDB.getRecyclerId());
        getAvailableRecyclerServiceCoupons(calculateActualOrderVo,availableCouponForm);

        calculateActualOrderVo.setTotalAmount(totalAmount);

        return calculateActualOrderVo;
    }

    /**
     * 根据订单id和订单状态修改订单状态
     * @param orderId
     * @param status
     * @return
     */
    @Override
    public Boolean updateOrderStatus(Long orderId, Integer status) {
        LambdaUpdateWrapper<OrderInfo> orderInfoLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        orderInfoLambdaUpdateWrapper.eq(OrderInfo::getId,orderId)
                .set(OrderInfo::getStatus,status)
                .set(OrderInfo::getUpdateTime,new Date());
        return this.update(orderInfoLambdaUpdateWrapper);
    }


    /**
     * 回收员校验回收码
     * @param validateRecycleCodeForm
     * @return
     */
    @Override
    public Boolean validateRecycleCode(ValidateRecycleCodeForm validateRecycleCodeForm) {
        String key = RedisConstant.RECYCLE_CODE + validateRecycleCodeForm.getOrderId();
        // 查询redis中订单回收码
        String redisRecycleCode = (String) redisTemplate.opsForValue().get(key);
        if (!validateRecycleCodeForm.getRecycleCode().equals(redisRecycleCode)) {
            return false;
        }
        // 修改订单状态
        this.updateOrderStatus(validateRecycleCodeForm.getOrderId(), OrderStatus.RECYCLER_UNPAID.getStatus());
        // 删除redis中的值
        redisTemplate.delete(key);
        return true;
    }

    /**
     * 结算订单
     * @param settlementForm
     * @return
     */
    @GlobalTransactional
    @Override
    public Boolean settlement(SettlementForm settlementForm) {
        // 查询订单信息
        OrderInfo orderInfoDB = this.getById(settlementForm.getOrderId());
        // 查询订单账单信息
        OrderBill billDB = orderBillService.getBillInfoByOrderId(settlementForm.getOrderId());
        // 查询回收员账户余额
        RecyclerAccountForm recyclerAccountForm = new RecyclerAccountForm();
        recyclerAccountForm.setRecyclerId(settlementForm.getRecyclerId());
        RecyclerAccountVo recyclerAccountVoDB = recyclerAccountFeignClient.getRecyclerAccountInfo(recyclerAccountForm).getData();
        // 如果回收员账户余额不够，则结算失败
        if (billDB.getRealRecyclerAmount().compareTo(recyclerAccountVoDB.getTotalAmount()) > 0) {
            throw new YueYiShouException(ResultCodeEnum.INSUFFICIENT_BALANCE);
        }
        // 减少回收员账户余额、增加账户明细、增加回收员单量
        RecyclerWithdrawForm recyclerWithdrawForm = new RecyclerWithdrawForm();
        recyclerWithdrawForm.setRecyclerId(settlementForm.getRecyclerId());
        recyclerWithdrawForm.setAmount(billDB.getRealRecyclerAmount());
        recyclerAccountFeignClient.settlement(recyclerWithdrawForm).getData();
        // 增加顾客账户余额、增加账户明细
        CustomerWithdrawForm customerWithdrawForm = new CustomerWithdrawForm();
        customerWithdrawForm.setCustomerId(orderInfoDB.getCustomerId());
        customerWithdrawForm.setAmount(billDB.getRealCustomerRecycleAmount());
        customerAccountFeignClient.settlement(customerWithdrawForm);
        // 更新账单信息
        UpdateBillForm updateBillForm = new UpdateBillForm();
        updateBillForm.setOrderId(settlementForm.getOrderId());
        updateBillForm.setPayTime(new Date());
        updateBillForm.setTransactionId(BillUtils.generateTransactionId());
        orderBillService.updateBill(updateBillForm);
        // 更新订单信息
        LambdaUpdateWrapper<OrderInfo> orderInfoLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        orderInfoLambdaUpdateWrapper.eq(OrderInfo::getId,settlementForm.getOrderId())
                .set(OrderInfo::getStatus,OrderStatus.COMPLETED_ORDER)
                .set(OrderInfo::getUpdateTime,new Date());
        return this.update(orderInfoLambdaUpdateWrapper);
    }

    /**
     * 计算回收员超时费用
     * @param calculateActualOrderVo
     * @param timeOutMin
     */
    private void calculateTimeoutFree(CalculateActualOrderVo calculateActualOrderVo,int timeOutMin){
        OvertimeRequestForm overtimeRequestForm = new OvertimeRequestForm();
        overtimeRequestForm.setOvertimeMinutes(timeOutMin);
        OvertimeResponseVo overtimeResponseVo = serviceFeeRuleFeignClient.calculateTimeoutFree(overtimeRequestForm).getData();
        calculateActualOrderVo.setRecyclerOvertimeCharges(overtimeResponseVo.getOvertimeFee());
    }

    /**
     * 计算订单实际回收总金额、回收员实际所付款手续费、顾客实际所付款手续费
     * @param calculateActualOrderVo
     * @param serviceFeeRuleRequestForm
     */
    private void calculateOrderAndFees(CalculateActualOrderVo calculateActualOrderVo, ServiceFeeRuleRequestForm serviceFeeRuleRequestForm) {
        ServiceFeeRuleResponseVo serviceFeeRuleResponseVo = serviceFeeRuleFeignClient.calculateOrderFee(serviceFeeRuleRequestForm).getData();
        calculateActualOrderVo.setRealRecyclerAmount(serviceFeeRuleResponseVo.getEstimatedTotalAmount());
        calculateActualOrderVo.setRealRecyclerPlatformAmount(serviceFeeRuleResponseVo.getExpectRecyclerPlatformAmount());
        calculateActualOrderVo.setRealCustomerPlatformAmount(serviceFeeRuleResponseVo.getExpectCustomerPlatformAmount());
    }

    /**
     * 获取在当前订单回收员可使用的服务抵扣劵
     * @param calculateActualOrderVo
     * @param availableCouponForm
     */
    private void getAvailableRecyclerServiceCoupons(CalculateActualOrderVo calculateActualOrderVo,
                                                                       AvailableCouponForm availableCouponForm) {
        calculateActualOrderVo.setRecyclerAvailableCouponList(recyclerCouponFeignClient.getAvailableCustomerServiceCoupons(availableCouponForm).getData());
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
