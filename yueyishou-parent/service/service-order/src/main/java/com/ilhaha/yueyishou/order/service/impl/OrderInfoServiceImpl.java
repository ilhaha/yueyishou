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
import com.ilhaha.yueyishou.model.constant.StartDisabledConstant;
import com.ilhaha.yueyishou.model.entity.order.OrderBill;
import com.ilhaha.yueyishou.model.entity.order.OrderInfo;
import com.ilhaha.yueyishou.model.entity.recycler.RecyclerInfo;
import com.ilhaha.yueyishou.model.enums.OrderStatus;
import com.ilhaha.yueyishou.model.enums.RecyclerAuthStatus;
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
import com.ilhaha.yueyishou.recycler.client.RecyclerInfoFeignClient;
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
import java.time.*;
import java.time.temporal.ChronoUnit;
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

    @Resource
    private RecyclerInfoFeignClient recyclerInfoFeignClient;


    /**
     * 订单分页列表查询
     *
     * @param orderMgrQueryForm
     * @param page
     * @return
     */
    @Override
    public Page<OrderMgrQueryVo> queryPageList(OrderMgrQueryForm orderMgrQueryForm, Page<OrderMgrQueryVo> page) {
        Page<OrderMgrQueryVo> orderMgrQueryVoPage = orderInfoMapper.queryPageList(page,
                orderMgrQueryForm, OrderStatus.RECYCLER_CONFIRM_ORDER.getStatus(),
                Arrays.asList(OrderConstant.NO_REJECTION_STATUS,
                        OrderConstant.REJECTION_APPLICATION_FAILED));
        // 待回收确认、待顾客确认临时计算回收员超时信息
        if (OrderStatus.RECYCLER_CONFIRM_ORDER.getStatus().compareTo(orderMgrQueryForm.getStatus()) <= 0
                && !OrderStatus.CANCELED_ORDER.getStatus().equals(orderMgrQueryForm.getStatus())) {
            orderMgrQueryVoPage.setRecords(orderMgrQueryVoPage.getRecords().stream().map(order -> {
                Integer timeOutMin = calculateTimeoutMinutes(order.getAppointmentTime(), order.getArriveTime());
                order.setArriveTimoutMin(timeOutMin);
                // 超时计算超时费用
                if (timeOutMin > 0 && OrderStatus.RECYCLER_CONFIRM_ORDER.getStatus().equals(orderMgrQueryForm.getStatus())) {
                    OvertimeRequestForm overtimeRequestForm = new OvertimeRequestForm();
                    overtimeRequestForm.setOvertimeMinutes(timeOutMin);
                    OvertimeResponseVo overtimeResponseVo = serviceFeeRuleFeignClient.calculateTimeoutFree(overtimeRequestForm).getData();
                    order.setRecyclerOvertimeCharges(overtimeResponseVo.getOvertimeFee());
                }
                return order;
            }).collect(Collectors.toList()));
        }
        return orderMgrQueryVoPage;
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
            redisTemplate.opsForValue().set(RedisConstant.WAITING_ORDER + orderInfo.getId(), orderInfo);
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
        LambdaQueryWrapper<OrderInfo> orderInfoLambdaQueryWrapper = new LambdaQueryWrapper<OrderInfo>();
        orderInfoLambdaQueryWrapper.eq(OrderInfo::getStatus, status)
                .eq(OrderInfo::getCustomerId, customerId)
                .eq(OrderInfo::getCustomerIsDeleted, PublicConstant.NOT_DELETED);
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
                        Integer timeOutMin = calculateTimeoutMinutes(item.getAppointmentTime(), item.getArriveTime());
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
     *
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
            Integer timoutMin = calculateTimeoutMinutes(customerOrderDetailsVo.getAppointmentTime(), customerOrderDetailsVo.getArriveTime());
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
            customerOrderDetailsVo.setRate(orderCommentVo.getRate());
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
                .set(OrderInfo::getCancelTime, new Date())
                .set(OrderInfo::getUpdateTime, new Date())
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
     *
     * @param recyclerId
     * @param orderId
     * @return
     */
    @Override
    public Boolean grabOrder(Long recyclerId, Long orderId) {
        // 查询回收员信息
        RecyclerInfo recyclerInfoDB = recyclerInfoFeignClient.queryById(recyclerId).getData();
        if (ObjectUtils.isEmpty(recyclerInfoDB)) {
            return false;
        }
        if (!RecyclerAuthStatus.CERTIFICATION_PASSED.getStatus().equals(recyclerInfoDB.getAuthStatus()) &&
                StartDisabledConstant.DISABLED_STATUS.equals(recyclerInfoDB.getStatus())) {
            throw new YueYiShouException(ResultCodeEnum.UNCERTIFIED_RECYCLER);
        }

        if (StartDisabledConstant.DISABLED_STATUS.equals(recyclerInfoDB.getStatus())) {
            throw new YueYiShouException(ResultCodeEnum.ACCOUNT_STOP);
        }

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
     *
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
            Integer timoutMin = calculateTimeoutMinutes(orderDetailsVo.getAppointmentTime(), orderDetailsVo.getArriveTime());
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
            orderDetailsVo.setRate(orderCommentVo.getRate());
        }

        return orderDetailsVo;
    }

    /**
     * 取消预约时间超时的订单
     *
     * @return
     */
    @Override
    public Boolean processTimeoutOrders(List<Long> timeoutOrderIds) {
        LambdaUpdateWrapper<OrderInfo> orderInfoLambdaUpdateChainWrapper = new LambdaUpdateWrapper<>();
        orderInfoLambdaUpdateChainWrapper.set(OrderInfo::getStatus, OrderStatus.CANCELED_ORDER)
                .set(OrderInfo::getCancelMessage, OrderConstant.TIMEOUT_CANCEL_REMARK)
                .set(OrderInfo::getCancelTime, new Date())
                .in(OrderInfo::getId, timeoutOrderIds);
        return this.update(orderInfoLambdaUpdateChainWrapper);
    }

    /**
     * 根据状态获取回收员订单列表
     *
     * @param recyclerId
     * @param status
     * @return
     */
    @Override
    public List<RecyclerOrderVo> getRecyclerOrderListByStatus(Long recyclerId, Integer status) {
        List<RecyclerOrderVo> result = new ArrayList<>();
        LambdaQueryWrapper<OrderInfo> orderInfoLambdaQueryWrapper = new LambdaQueryWrapper<OrderInfo>();
        orderInfoLambdaQueryWrapper.eq(OrderInfo::getRecyclerId, recyclerId)
                .eq(OrderInfo::getStatus, status)
                .eq(OrderInfo::getRecyclerIsDeleted, PublicConstant.NOT_DELETED);
        List<OrderInfo> orderInfoListDB = this.list(orderInfoLambdaQueryWrapper);
        if (ObjectUtils.isEmpty(orderInfoListDB)) {
            return result;
        }
        Point recyclerLocationFromRedis = getRecyclerLocationFromRedis(recyclerId);
        result = orderInfoListDB.stream().map(order -> {
            RecyclerOrderVo recyclerOrderVo = new RecyclerOrderVo();
            BeanUtils.copyProperties(order, recyclerOrderVo);
            recyclerOrderVo.setActualPhoto(order.getActualPhotos().split(",")[0]);
            if (OrderStatus.RECYCLER_ACCEPTED.getStatus().equals(status) && !ObjectUtils.isEmpty(recyclerLocationFromRedis)) {
                // 计算回收员距离订单多远
                CalculateLineForm calculateLineForm = new CalculateLineForm();
                calculateLineForm.setRecyclerId(recyclerId);
                calculateLineForm.setEndPointLatitude(order.getCustomerPointLatitude());
                calculateLineForm.setEndPointLongitude(order.getCustomerPointLongitude());
                DrivingLineVo drivingLineVo = mapFeignClient.calculateDrivingLine(calculateLineForm).getData();
                recyclerOrderVo.setApart(ObjectUtils.isEmpty(drivingLineVo) ? BigDecimal.ZERO : drivingLineVo.getDistance());
            }
            if (!ObjectUtils.isEmpty(recyclerOrderVo.getArriveTime())) {
                // 计算回收员是否超时，并计算超时多少分钟
                Integer timoutMin = calculateTimeoutMinutes(recyclerOrderVo.getAppointmentTime(), recyclerOrderVo.getArriveTime());
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
     *
     * @param appointmentTime 预约时间，格式为 yyyy-MM-dd HH:mm:ss
     * @param arriveTime      到达时间，格式为 yyyy-MM-dd HH:mm:ss
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
     *
     * @param orderId
     * @return
     */
    @Override
    public Boolean repost(Long orderId) {
        // 更新订单信息
        LambdaUpdateWrapper<OrderInfo> orderInfoLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        orderInfoLambdaUpdateWrapper.set(OrderInfo::getRecyclerId, null)
                .set(OrderInfo::getStatus, OrderStatus.WAITING_ACCEPT)
                .set(OrderInfo::getAcceptTime, null)
                .eq(OrderInfo::getId, orderId);
        Boolean flag = this.update(orderInfoLambdaUpdateWrapper);
        if (flag) {
            // 重新查出订单信息
            OrderInfo orderInfoDB = this.getById(orderId);
            // 重新存入redis
            redisTemplate.opsForValue().set(RedisConstant.WAITING_ORDER + orderId, orderInfoDB);
        }
        return flag;
    }

    /**
     * 回收员到达回收点
     *
     * @param orderId
     * @return
     */
    @Override
    public Boolean arrive(Long orderId) {
        //TODO 之后要完善不能超过100米就点击已到达
        LambdaUpdateWrapper<OrderInfo> orderInfoLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        orderInfoLambdaUpdateWrapper.set(OrderInfo::getStatus, OrderStatus.RECYCLER_CONFIRM_ORDER)
                .set(OrderInfo::getArriveTime, new Date())
                .eq(OrderInfo::getId, orderId);
        return this.update(orderInfoLambdaUpdateWrapper);
    }

    /**
     * 更新订单信息
     *
     * @param updateOrderFrom
     * @return
     */
    @Override
    public Boolean updateOrder(UpdateOrderFrom updateOrderFrom) {
        OrderInfo orderInfo = new OrderInfo();
        BeanUtils.copyProperties(updateOrderFrom, orderInfo);
        orderInfo.setUpdateTime(new Date());
        return this.updateById(orderInfo);
    }

    /**
     * 计算实际的订单信息
     *
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
        calculateOrderAndFees(calculateActualOrderVo, serviceFeeRuleRequestForm);
        totalAmount = totalAmount.add(calculateActualOrderVo.getRealRecyclerAmount()).add(calculateActualOrderVo.getRealRecyclerPlatformAmount());

        // 判断回收员是否超时
        int timeOutMin = calculateTimeoutMinutes(orderInfoDB.getAppointmentTime(), orderInfoDB.getArriveTime());
        // 已超时
        if (OrderConstant.NOT_TIMED_OUT_MIN < timeOutMin) {
            // 计算回收员需要付的超时费用
            calculateTimeoutFree(calculateActualOrderVo, timeOutMin);
            calculateActualOrderVo.setTimeOutMin(timeOutMin);
            totalAmount = totalAmount.add(calculateActualOrderVo.getRecyclerOvertimeCharges());
        }
        // 获取回收员可使用的服务抵扣劵
        AvailableCouponForm availableCouponForm = new AvailableCouponForm();
        availableCouponForm.setRealRecyclerAmount(calculateActualOrderVo.getRealRecyclerAmount());
        availableCouponForm.setRecyclerId(orderInfoDB.getRecyclerId());
        getAvailableRecyclerServiceCoupons(calculateActualOrderVo, availableCouponForm);

        calculateActualOrderVo.setTotalAmount(totalAmount);

        return calculateActualOrderVo;
    }

    /**
     * 根据订单id和订单状态修改订单状态
     *
     * @param orderId
     * @param status
     * @return
     */
    @Override
    public Boolean updateOrderStatus(Long orderId, Integer status) {
        LambdaUpdateWrapper<OrderInfo> orderInfoLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        orderInfoLambdaUpdateWrapper.eq(OrderInfo::getId, orderId)
                .set(OrderInfo::getStatus, status)
                .set(OrderInfo::getUpdateTime, new Date());
        return this.update(orderInfoLambdaUpdateWrapper);
    }


    /**
     * 回收员校验回收码
     *
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
     *
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
        orderInfoLambdaUpdateWrapper.eq(OrderInfo::getId, settlementForm.getOrderId())
                .set(OrderInfo::getStatus, OrderStatus.COMPLETED_ORDER)
                .set(OrderInfo::getUpdateTime, new Date());
        return this.update(orderInfoLambdaUpdateWrapper);
    }

    /***
     * 结算取消订单费用
     * @param cancelOrderForm
     * @return
     */
    @Override
    public CancelOrderFeeVo calculateCancellationFee(CancelOrderFeeForm cancelOrderForm) {
        CancelOrderFeeVo cancelOrderVo = new CancelOrderFeeVo();
        // 当前时间是否大于预约上门时间，如果大于则需要回收员支付取消
        OvertimeRequestForm overtimeRequestForm = new OvertimeRequestForm();
        long serviceOvertimeMin = calculateMinutesDifference(new Date(), cancelOrderForm.getAppointmentTime());
        if (serviceOvertimeMin < 0) {
            // 当前时间大于预约上门时间，需回收员付款取消
            overtimeRequestForm.setOvertimeMinutes(Math.toIntExact(Math.abs(serviceOvertimeMin)));
            OvertimeResponseVo overtimeResponseVo = serviceFeeRuleFeignClient.calculateTimeoutCancelFree(overtimeRequestForm).getData();
            cancelOrderVo.setServiceOvertimePenalty(overtimeResponseVo.getOvertimeFee());
            cancelOrderVo.setOvertimeMinutes(overtimeRequestForm.getOvertimeMinutes());
            return cancelOrderVo;
        }

        // 顾客取消订单规则
        if (OrderConstant.CUSTOMER_CANCELS_ORDER.equals(cancelOrderForm.getCancelOperator())) {
            // 如果当前时间与回收员接单时间超过五分钟，需付费取消
            long customerLateCancellationMin = calculateMinutesDifference(new Date(), cancelOrderForm.getAcceptTime());
            if (customerLateCancellationMin < 0 &&
                    OrderConstant.CUSTOMER_CANCELS_ORDER_FREE_MIN < Math.abs(customerLateCancellationMin)) {
                overtimeRequestForm.setOvertimeMinutes(Math.toIntExact(Math.abs(customerLateCancellationMin) - OrderConstant.CUSTOMER_CANCELS_ORDER_FREE_MIN));
                OvertimeResponseVo overtimeResponseVo = serviceFeeRuleFeignClient.customerLateCancellationFee(overtimeRequestForm).getData();
                cancelOrderVo.setCustomerLateCancellationFee(overtimeResponseVo.getOvertimeFee());
                cancelOrderVo.setOvertimeMinutes(overtimeRequestForm.getOvertimeMinutes());
            }
            return cancelOrderVo;
        }
        // 回收员取消规则
        if (OrderConstant.RECYCLER_CANCELS_ORDER.equals(cancelOrderForm.getCancelOperator())) {
            // 如果当前时间距离预约上门时间不足60分钟，需付费取消
            long recyclerLateCancellationMin = calculateMinutesDifference(new Date(), cancelOrderForm.getAppointmentTime());
            if (OrderConstant.RECYCLER_CANCELS_ORDER_FREE_MIN > recyclerLateCancellationMin) {
                overtimeRequestForm.setOvertimeMinutes(Math.toIntExact(OrderConstant.RECYCLER_CANCELS_ORDER_FREE_MIN - recyclerLateCancellationMin));
                OvertimeResponseVo overtimeResponseVo = serviceFeeRuleFeignClient.recyclerLateCancellationFee(overtimeRequestForm).getData();
                cancelOrderVo.setRecyclerLateCancellationFee(overtimeResponseVo.getOvertimeFee());
                cancelOrderVo.setOvertimeMinutes(overtimeRequestForm.getOvertimeMinutes());
            }
        }
        return cancelOrderVo;
    }

    /**
     * 回收员接单后，回收员、顾客取消订单
     *
     * @param cancelOrderForm
     * @return
     */
    @GlobalTransactional
    @Override
    public Boolean cancelOrderAfterTaking(CancelOrderForm cancelOrderForm) {
        LambdaUpdateWrapper<OrderInfo> orderInfoLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        orderInfoLambdaUpdateWrapper.eq(OrderInfo::getId, cancelOrderForm.getOrderId())
                .set(OrderInfo::getCancelTime, new Date())
                .set(OrderInfo::getUpdateTime, new Date())
                .set(OrderInfo::getStatus, OrderStatus.CANCELED_ORDER);
        // 超过预约时间，回收员支付取消
        if (!ObjectUtils.isEmpty(cancelOrderForm.getServiceOvertimePenalty())) {
            orderInfoLambdaUpdateWrapper.set(OrderInfo::getCancelMessage, OrderConstant.RECYCLER_LATE_CANCELLATION_NOTE)
                    .set(OrderInfo::getServiceOvertimePenalty, cancelOrderForm.getServiceOvertimePenalty());

            // 减少回收员账户余额,新增回收员的账户明细
            RecyclerWithdrawForm recyclerWithdrawForm = new RecyclerWithdrawForm();
            recyclerWithdrawForm.setAmount(cancelOrderForm.getServiceOvertimePenalty());
            recyclerWithdrawForm.setRecyclerId(cancelOrderForm.getRecyclerId());
            recyclerAccountFeignClient.cancelOrderIfOverdue(recyclerWithdrawForm);

            // 增加顾客账户余额，新增顾客的账户明细
            CustomerWithdrawForm customerWithdrawForm = new CustomerWithdrawForm();
            customerWithdrawForm.setCustomerId(cancelOrderForm.getCustomerId());
            customerWithdrawForm.setAmount(cancelOrderForm.getServiceOvertimePenalty());
            customerAccountFeignClient.cancelOrderIfOverdue(customerWithdrawForm);

            return this.update(orderInfoLambdaUpdateWrapper);
        }
        // 顾客在回收员接单超五分钟之后付费取消
        if (!ObjectUtils.isEmpty(cancelOrderForm.getCustomerLateCancellationFee())) {
            orderInfoLambdaUpdateWrapper.set(OrderInfo::getCancelMessage, OrderConstant.CANCEL_REMARK)
                    .set(OrderInfo::getCustomerLateCancellationFee, cancelOrderForm.getCustomerLateCancellationFee());

            // 增加回收员账户余额,新增回收员的账户明细
            RecyclerWithdrawForm recyclerWithdrawForm = new RecyclerWithdrawForm();
            recyclerWithdrawForm.setAmount(cancelOrderForm.getCustomerLateCancellationFee());
            recyclerWithdrawForm.setRecyclerId(cancelOrderForm.getRecyclerId());
            recyclerAccountFeignClient.processPaidCancellation(recyclerWithdrawForm);

            // 减少顾客账户余额，新增顾客的账户明细
            CustomerWithdrawForm customerWithdrawForm = new CustomerWithdrawForm();
            customerWithdrawForm.setCustomerId(cancelOrderForm.getCustomerId());
            customerWithdrawForm.setAmount(cancelOrderForm.getCustomerLateCancellationFee());
            customerAccountFeignClient.processPaidCancellation(customerWithdrawForm);

            return this.update(orderInfoLambdaUpdateWrapper);
        }
        // 回收员在预约时间与当前时间不足60分钟付费取消
        if (!ObjectUtils.isEmpty(cancelOrderForm.getRecyclerLateCancellationFee())) {
            orderInfoLambdaUpdateWrapper.set(OrderInfo::getCancelMessage, OrderConstant.SHORT_NOTICE_CANCELLATION)
                    .set(OrderInfo::getRecyclerLateCancellationFee, cancelOrderForm.getRecyclerLateCancellationFee());

            // 减少回收员账户余额,新增回收员的账户明细
            RecyclerWithdrawForm recyclerWithdrawForm = new RecyclerWithdrawForm();
            recyclerWithdrawForm.setAmount(cancelOrderForm.getRecyclerLateCancellationFee());
            recyclerWithdrawForm.setRecyclerId(cancelOrderForm.getRecyclerId());
            recyclerAccountFeignClient.chargeCancellationIfWithinOneHour(recyclerWithdrawForm);

            // 增加顾客账户余额，新增顾客的账户明细
            CustomerWithdrawForm customerWithdrawForm = new CustomerWithdrawForm();
            customerWithdrawForm.setCustomerId(cancelOrderForm.getCustomerId());
            customerWithdrawForm.setAmount(cancelOrderForm.getRecyclerLateCancellationFee());
            customerAccountFeignClient.chargeCancellationIfWithinOneHour(customerWithdrawForm);

            return this.update(orderInfoLambdaUpdateWrapper);
        }
        return false;
    }

    /**
     * 删除订单
     *
     * @param orderDeleteForm
     * @return
     */
    @Override
    public Boolean delete(OrderDeleteForm orderDeleteForm) {
        LambdaUpdateWrapper<OrderInfo> orderInfoLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        orderInfoLambdaUpdateWrapper.eq(!ObjectUtils.isEmpty(orderDeleteForm.getCustomerId()), OrderInfo::getCustomerId, orderDeleteForm.getCustomerId())
                .eq(!ObjectUtils.isEmpty(orderDeleteForm.getRecyclerId()), OrderInfo::getRecyclerId, orderDeleteForm.getRecyclerId())
                .eq(OrderInfo::getId, orderDeleteForm.getOrderId())
                .set(!ObjectUtils.isEmpty(orderDeleteForm.getCustomerId()), OrderInfo::getCustomerIsDeleted, PublicConstant.DELETED)
                .set(!ObjectUtils.isEmpty(orderDeleteForm.getRecyclerId()), OrderInfo::getRecyclerIsDeleted, PublicConstant.DELETED)
                .set(OrderInfo::getUpdateTime, new Date());
        return this.update(orderInfoLambdaUpdateWrapper);
    }

    /**
     * 获取顾客我的页面的订单初始化信息
     *
     * @param customerId
     * @return
     */
    @Override
    public OrderMyVo getMy(Long customerId) {
        OrderMyVo orderMyVo = new OrderMyVo();
        orderMyVo.setDeliveryVolume(new BigDecimal(BigInteger.ZERO));
        orderMyVo.setRecyclerCount(0);
        LambdaQueryWrapper<OrderInfo> orderInfoLambdaQueryWrapper = new LambdaQueryWrapper<OrderInfo>();
        orderInfoLambdaQueryWrapper.eq(OrderInfo::getCustomerId, customerId)
                .in(OrderInfo::getStatus, Arrays.asList(OrderStatus.COMPLETED_ORDER.getStatus(), OrderStatus.AWAITING_EVALUATION.getStatus()));
        List<OrderInfo> list = this.list(orderInfoLambdaQueryWrapper);
        if (!ObjectUtils.isEmpty(list)) {
            orderMyVo.setRecyclerCount(list.size());
            for (OrderInfo orderInfo : list) {
                orderMyVo.setDeliveryVolume(orderInfo.getRecycleWeigh().add(orderMyVo.getDeliveryVolume()));
            }
        }
        return orderMyVo;
    }

    /**
     * 后台管理系统汇总数据
     *
     * @return
     */
    @Override
    public CollectVo collect() {
        CollectVo collectVo = new CollectVo();
        List<OrderInfo> orderInfoListDB = this.list(null);
        // 计算订单总量、本周订单量、每日订单量
        calculateOrderStatistics(collectVo, orderInfoListDB);

        orderInfoListDB = orderInfoListDB.stream().filter(item -> {
            return item.getStatus().equals(OrderStatus.COMPLETED_ORDER.getStatus()) ||
                    item.getStatus().equals(OrderStatus.AWAITING_EVALUATION.getStatus());
        }).collect(Collectors.toList());

        if (!ObjectUtils.isEmpty(orderInfoListDB)) {
            List<Long> orderIds = orderInfoListDB.stream().map(OrderInfo::getId).collect(Collectors.toList());
            List<OrderBill> orderBillListDB = orderBillService.getBillInfoByOrderIds(orderIds);

            // 计算总佣金收入、今日佣金收入、同日比、同周比
            calculateAdditionalMetrics(collectVo, orderBillListDB);

            // 计算总支付订单数、本周每日订单支付数、支付转化率
            calculateOrderPayStatistics(collectVo, orderBillListDB);

            // 计算本年每个月的佣金收入
            calculateMonthlyCommissionIncome(collectVo, orderBillListDB);

            // 计算今日每个时辰的佣金收入
            calculateHourlyCommissionIncome(collectVo, orderBillListDB);

            // 计算本周每日的佣金收入
            calculateWeeklyDailyCommissionIncome(collectVo, orderBillListDB);

            // 计算本月每日的佣金收入
            calculateMonthlyDailyCommissionIncome(collectVo, orderBillListDB);
        }

        return collectVo;
    }


    /**
     * 回收员拒收订单
     *
     * @param rejectOrderForm
     * @return
     */
    @Override
    public Boolean reject(RejectOrderForm rejectOrderForm) {
        // 计算回收员已服务多久（分钟）
        long serviceMin = calculateMinutesDifference(rejectOrderForm.getAcceptTime(), rejectOrderForm.getArriveTime());
        // 计算拒收之后得到的补偿
        OvertimeRequestForm overtimeRequestForm = new OvertimeRequestForm();
        overtimeRequestForm.setOvertimeMinutes(Math.toIntExact(serviceMin));
        OvertimeResponseVo overtimeResponseVo = serviceFeeRuleFeignClient.calculateRejectionCompensation(overtimeRequestForm).getData();
        // 更新订单信息
        LambdaUpdateWrapper<OrderInfo> orderInfoLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        orderInfoLambdaUpdateWrapper.eq(OrderInfo::getId, rejectOrderForm.getOrderId())
                .set(OrderInfo::getRejectStatus, OrderConstant.DENIED_STATUS)
                .set(OrderInfo::getCancelTime, new Date())
                .set(OrderInfo::getCancelMessage, rejectOrderForm.getCancelMessage())
                .set(OrderInfo::getRejectActualPhotos, rejectOrderForm.getRejectActualPhotos())
                .set(OrderInfo::getUpdateTime, new Date())
                .set(OrderInfo::getRejectCompensation, ObjectUtils.isEmpty(overtimeResponseVo.getOvertimeFee()) ? BigDecimal.ZERO : overtimeResponseVo.getOvertimeFee());
        return this.update(orderInfoLambdaUpdateWrapper);
    }

    /**
     * 获取拒收订单信息
     *
     * @param orderId
     * @return
     */
    @Override
    public RejectOrderVo getRejectInfo(Long orderId) {
        RejectOrderVo rejectOrderVo = new RejectOrderVo();
        OrderInfo orderInfo = this.getById(orderId);
        BeanUtils.copyProperties(orderInfo, rejectOrderVo);
        return rejectOrderVo;
    }

    /**
     * 取消申请订单拒收
     *
     * @param orderId
     * @return
     */
    @Override
    public Boolean cancelReject(Long orderId) {
        LambdaUpdateWrapper<OrderInfo> orderInfoLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        orderInfoLambdaUpdateWrapper.eq(OrderInfo::getId, orderId)
                .set(OrderInfo::getCancelMessage, null)
                .set(OrderInfo::getCancelTime, null)
                .set(OrderInfo::getRejectStatus, OrderConstant.NO_REJECTION_STATUS)
                .set(OrderInfo::getRejectActualPhotos, null)
                .set(OrderInfo::getUpdateTime, new Date());
        return this.update(orderInfoLambdaUpdateWrapper);
    }

    /**
     * 获取申请拒收订单列表
     * @param page
     * @param rejectOrderListForm
     * @return
     */
    @Override
    public Page<RejectOrderListVo> getRejectOrderList(Page<RejectOrderListVo> page, RejectOrderListForm rejectOrderListForm) {
        return orderInfoMapper.getRejectOrderList(page, OrderConstant.DENIED_STATUS,rejectOrderListForm);
    }

    /**
     * 审批拒收申请
     *
     * @param approvalRejectOrderForm
     * @return
     */
    @GlobalTransactional
    @Override
    public Boolean approvalReject(ApprovalRejectOrderForm approvalRejectOrderForm) {
        LambdaUpdateWrapper<OrderInfo> orderInfoLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        // 申请通过
        if (OrderConstant.REJECT_APPLICATION_STATUS.equals(approvalRejectOrderForm.getRejectStatus())) {
            orderInfoLambdaUpdateWrapper.eq(OrderInfo::getId, approvalRejectOrderForm.getOrderId())
                    .set(OrderInfo::getRejectStatus, approvalRejectOrderForm.getRejectStatus())
                    .set(OrderInfo::getStatus, OrderStatus.CANCELED_ORDER)
                    .set(OrderInfo::getUpdateTime, new Date());
            // 增加回收员账户余额、减少顾客账户余额
            RecyclerWithdrawForm recyclerWithdrawForm = new RecyclerWithdrawForm();
            recyclerWithdrawForm.setAmount(approvalRejectOrderForm.getRejectCompensation());
            recyclerWithdrawForm.setRecyclerId(approvalRejectOrderForm.getRecyclerId());
            recyclerAccountFeignClient.rejectCompensate(recyclerWithdrawForm);

            CustomerWithdrawForm customerWithdrawForm = new CustomerWithdrawForm();
            customerWithdrawForm.setAmount(approvalRejectOrderForm.getRejectCompensation());
            customerWithdrawForm.setCustomerId(approvalRejectOrderForm.getCustomerId());
            customerAccountFeignClient.rejectCompensate(customerWithdrawForm);


            // 驳回申请
        } else {
            orderInfoLambdaUpdateWrapper.eq(OrderInfo::getId, approvalRejectOrderForm.getOrderId())
                    .set(OrderInfo::getRejectStatus, OrderConstant.REJECTION_APPLICATION_FAILED)
                    .set(OrderInfo::getUpdateTime, new Date());
        }
        return this.update(orderInfoLambdaUpdateWrapper);
    }

    /**
     * 计算本月每日的佣金收入
     *
     * @param collectVo
     * @param orderBillListDB
     */
    private void calculateMonthlyDailyCommissionIncome(CollectVo collectVo, List<OrderBill> orderBillListDB) {
        // 初始化 Map 来存储本月每天的收入
        Map<String, BigDecimal> dailyIncomeMap = new LinkedHashMap<>();

        // 获取本月的第一天和今天的日期
        LocalDate today = LocalDate.now();
        LocalDate startOfMonth = today.withDayOfMonth(1);

        // 初始化本月每一天的收入为 0
        for (LocalDate date = startOfMonth; !date.isAfter(today); date = date.plusDays(1)) {
            dailyIncomeMap.put(date.toString(), BigDecimal.ZERO);
        }

        // 遍历订单账单，计算本月每一天的收入
        orderBillListDB.forEach(orderBill -> {
            LocalDate orderDate = convertToLocalDate(orderBill.getPayTime());

            // 仅累加本月内的订单收入
            if (!orderDate.isBefore(startOfMonth) && !orderDate.isAfter(today)) {
                BigDecimal dailyIncome = dailyIncomeMap.getOrDefault(orderDate.toString(), BigDecimal.ZERO);
                BigDecimal orderIncome = orderBill.getRealRecyclerPlatformAmount().add(orderBill.getRealCustomerPlatformAmount());

                // 更新对应日期的总收入
                dailyIncomeMap.put(orderDate.toString(), dailyIncome.add(orderIncome));
            }
        });

        // 转换为 List<Map<String, Object>> 格式存入 CollectVo
        List<Map<String, Object>> monthCommissionIncomeList = new ArrayList<>();
        dailyIncomeMap.forEach((date, income) -> {
            Map<String, Object> dailyIncomeEntry = new HashMap<>();
            dailyIncomeEntry.put("x", date);  // "x" 为日期
            dailyIncomeEntry.put("y", income);  // "y" 为佣金收入
            monthCommissionIncomeList.add(dailyIncomeEntry);
        });

        collectVo.setMonthCommissionIncome(monthCommissionIncomeList);
    }

    /**
     * 计算本周每日的佣金收入
     *
     * @param collectVo
     * @param orderBillListDB
     */
    private void calculateWeeklyDailyCommissionIncome(CollectVo collectVo, List<OrderBill> orderBillListDB) {
        // 初始化 Map 来存储本周每天的收入
        Map<String, BigDecimal> dailyIncomeMap = new LinkedHashMap<>();

        // 获取本周的周一日期
        LocalDate startOfWeek = LocalDate.now().with(DayOfWeek.MONDAY);

        // 初始化本周 7 天的收入为 0
        for (int i = 0; i < 7; i++) {
            LocalDate date = startOfWeek.plusDays(i);
            dailyIncomeMap.put(date.toString(), BigDecimal.ZERO);
        }

        // 遍历订单账单，按本周每一天累加收入
        orderBillListDB.forEach(orderBill -> {
            LocalDate orderDate = convertToLocalDate(orderBill.getPayTime());

            // 仅累加本周内的订单收入
            if (!orderDate.isBefore(startOfWeek) && !orderDate.isAfter(startOfWeek.plusDays(6))) {
                BigDecimal dailyIncome = dailyIncomeMap.getOrDefault(orderDate.toString(), BigDecimal.ZERO);
                BigDecimal orderIncome = orderBill.getRealRecyclerPlatformAmount().add(orderBill.getRealCustomerPlatformAmount());

                // 更新对应日期的总收入
                dailyIncomeMap.put(orderDate.toString(), dailyIncome.add(orderIncome));
            }
        });

        // 转换为 List<Map<String, Object>> 格式存入 CollectVo
        List<Map<String, Object>> dailyCommissionIncomeList = new ArrayList<>();
        dailyIncomeMap.forEach((date, income) -> {
            Map<String, Object> dailyIncomeEntry = new HashMap<>();
            dailyIncomeEntry.put(PublicConstant.INDEX_ORDER_COLLECT_X, date);  // "x" 为日期
            dailyIncomeEntry.put(PublicConstant.INDEX_ORDER_COLLECT_Y, income);  // "y" 为佣金收入
            dailyCommissionIncomeList.add(dailyIncomeEntry);
        });

        collectVo.setWeekCommissionIncome(dailyCommissionIncomeList);
    }

    /**
     * 计算今日每个时辰的佣金收入
     *
     * @param collectVo
     * @param orderBillListDB
     */
    private void calculateHourlyCommissionIncome(CollectVo collectVo, List<OrderBill> orderBillListDB) {
        // 初始化 Map 来存储今日每个小时的收入
        Map<String, BigDecimal> hourlyIncomeMap = new LinkedHashMap<>();

        // 获取今天的日期
        LocalDate today = LocalDate.now();

        // 初始化每个小时的收入为0
        for (int hour = 0; hour < 24; hour++) {
            String hourKey = String.format("%02d", hour); // 保留小时数（不带分钟）
            hourlyIncomeMap.put(hourKey, BigDecimal.ZERO);
        }

        // 遍历订单账单，计算每小时的收入
        orderBillListDB.forEach(orderBill -> {
            LocalDateTime payDateTime = convertToLocalDateTime(orderBill.getPayTime());
            LocalDate payDate = payDateTime.toLocalDate();

            // 仅计算今天的订单
            if (payDate.isEqual(today)) {
                int hour = payDateTime.getHour();
                String hourKey = String.format("%02d", hour); // 保留小时数

                BigDecimal hourlyIncome = hourlyIncomeMap.getOrDefault(hourKey, BigDecimal.ZERO);
                BigDecimal orderIncome = orderBill.getRealRecyclerPlatformAmount().add(orderBill.getRealCustomerPlatformAmount());

                // 更新当前小时的总收入
                hourlyIncomeMap.put(hourKey, hourlyIncome.add(orderIncome));
            }
        });

        // 转换为 List<Map<String, Object>> 格式存入 CollectVo
        List<Map<String, Object>> timeCommissionIncomeList = new ArrayList<>();
        hourlyIncomeMap.forEach((hour, income) -> {
            Map<String, Object> hourlyIncomeEntry = new HashMap<>();
            hourlyIncomeEntry.put(PublicConstant.INDEX_ORDER_COLLECT_X, hour + PublicConstant.HOUR_UNIT);
            hourlyIncomeEntry.put(PublicConstant.INDEX_ORDER_COLLECT_Y, income);
            timeCommissionIncomeList.add(hourlyIncomeEntry);
        });

        collectVo.setTimeCommissionIncome(timeCommissionIncomeList);
    }

    /**
     * 计算今年每个月的佣金收入
     *
     * @param collectVo
     * @param orderBillListDB
     */
    private void calculateMonthlyCommissionIncome(CollectVo collectVo, List<OrderBill> orderBillListDB) {
        // 初始化一个 Map 来存储每个月的收入
        Map<String, BigDecimal> monthlyIncomeMap = new LinkedHashMap<>();

        // 获取当前年份
        int currentYear = LocalDate.now().getYear();

        // 初始化每个月的收入为 0
        for (int month = 1; month <= 12; month++) {
            monthlyIncomeMap.put(String.format("%d-%02d", currentYear, month), BigDecimal.ZERO);
        }

        // 遍历订单账单，按月份累加收入
        orderBillListDB.forEach(orderBill -> {
            LocalDate orderDate = convertToLocalDate(orderBill.getPayTime());

            // 仅计算当前年份的订单
            if (orderDate.getYear() == currentYear) {
                String monthKey = String.format("%d-%02d", currentYear, orderDate.getMonthValue());
                BigDecimal monthlyIncome = monthlyIncomeMap.get(monthKey);
                BigDecimal orderIncome = orderBill.getRealRecyclerPlatformAmount().add(orderBill.getRealCustomerPlatformAmount());

                // 更新当前月份的总收入
                monthlyIncomeMap.put(monthKey, monthlyIncome.add(orderIncome));
            }
        });

        // 转换为 List<Map<String, Object>> 格式存入 CollectVo
        List<Map<String, Object>> monthlyCommissionIncomeList = new ArrayList<>();
        monthlyIncomeMap.forEach((month, income) -> {
            Map<String, Object> monthlyIncomeEntry = new HashMap<>();
            monthlyIncomeEntry.put(PublicConstant.INDEX_ORDER_COLLECT_X, month);
            monthlyIncomeEntry.put(PublicConstant.INDEX_ORDER_COLLECT_Y, income);
            monthlyCommissionIncomeList.add(monthlyIncomeEntry);
        });

        collectVo.setYearCommissionIncome(monthlyCommissionIncomeList);
    }

    /**
     * 计算总支付订单数、本周每日订单支付数、支付转化率
     *
     * @param collectVo
     * @param orderBillListDB
     */
    private void calculateOrderPayStatistics(CollectVo collectVo, List<OrderBill> orderBillListDB) {
        // 订单总支付数
        collectVo.setTotalOrderPayCount(Long.valueOf(orderBillListDB.size()));

        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = today.with(DayOfWeek.MONDAY);

        // 计算本周每日订单支付量
        collectVo.setDailyOrderPayCountMap(calculateDailyOrderPayCount(orderBillListDB, startOfWeek, today));

        // 本周订单支付量
        BigDecimal currentWeekOrderPayCount = BigDecimal.valueOf(calculateOrderPayCountForRange(orderBillListDB, startOfWeek, today));
        // 计算支付转换率
        if (collectVo.getCurrentWeekOrderCount() != 0) {
            collectVo.setConversionRate(
                    currentWeekOrderPayCount
                            .divide(BigDecimal.valueOf(collectVo.getCurrentWeekOrderCount()), 4, RoundingMode.HALF_UP)
                            .multiply(BigDecimal.valueOf(100)));
        } else {
            // 如果分母为0，设置转换率为0或者其他默认值
            collectVo.setConversionRate(BigDecimal.ZERO);
        }

    }

    /**
     * 计算订单总量、本周订单量、每日订单量
     *
     * @param collectVo
     * @param orderInfoListDB
     */
    private void calculateOrderStatistics(CollectVo collectVo, List<OrderInfo> orderInfoListDB) {
        // 订单总量
        collectVo.setTotalOrderCount(Long.valueOf(orderInfoListDB.size()));

        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = today.with(DayOfWeek.MONDAY);

        // 计算本周订单量
        collectVo.setCurrentWeekOrderCount(calculateOrderCountForRange(orderInfoListDB, startOfWeek, today));
        // 计算本周每日订单量
        collectVo.setDailyOrderCountMap(calculateDailyOrderCount(orderInfoListDB, startOfWeek, today));
    }

    /**
     * 计算这周每日的订单量
     *
     * @param orderInfoListDB
     * @param startOfWeek
     * @param today
     * @return
     */
    private List<Map<String, Object>> calculateDailyOrderCount(List<OrderInfo> orderInfoListDB, LocalDate startOfWeek, LocalDate today) {
        ArrayList<Map<String, Object>> result = new ArrayList<>();
        // 循环遍历从 startOfWeek 到 today 的每一天
        while (!startOfWeek.isAfter(today)) {
            Map<String, Object> dailyOrderCountMap = new HashMap<>();
            // 将当前的 startOfWeek 保存到一个局部变量中，以便在 lambda 表达式中使用
            LocalDate currentDate = startOfWeek;
            dailyOrderCountMap.put(PublicConstant.INDEX_ORDER_COLLECT_X, currentDate);

            // 计算该日期的订单数量
            long count = orderInfoListDB.stream()
                    .filter(order -> convertToLocalDate(order.getCreateTime()).isEqual(currentDate))
                    .count();

            // 将日期和订单数量存入 Map 中
            dailyOrderCountMap.put(PublicConstant.INDEX_ORDER_COLLECT_Y, count);
            // 日期加一天，进入下一天的统计
            startOfWeek = startOfWeek.plusDays(1);
            result.add(dailyOrderCountMap);
        }
        return result;
    }

    /**
     * 计算本周每日的订单支付量
     *
     * @param orderBillListDB
     * @param startOfWeek
     * @param today
     * @return
     */
    private List<Map<String, Object>> calculateDailyOrderPayCount(List<OrderBill> orderBillListDB, LocalDate startOfWeek, LocalDate today) {
        ArrayList<Map<String, Object>> result = new ArrayList<>();
        // 循环遍历从 startOfWeek 到 today 的每一天
        while (!startOfWeek.isAfter(today)) {
            Map<String, Object> dailyOrderCountMap = new HashMap<>();
            // 将当前的 startOfWeek 保存到一个局部变量中，以便在 lambda 表达式中使用
            LocalDate currentDate = startOfWeek;
            dailyOrderCountMap.put(PublicConstant.INDEX_ORDER_COLLECT_X, currentDate);

            // 计算该日期的订单数量
            long count = orderBillListDB.stream()
                    .filter(order -> convertToLocalDate(order.getPayTime()).isEqual(currentDate))
                    .count();

            // 将日期和订单数量存入 Map 中
            dailyOrderCountMap.put(PublicConstant.INDEX_ORDER_COLLECT_Y, count);
            // 日期加一天，进入下一天的统计
            startOfWeek = startOfWeek.plusDays(1);
            result.add(dailyOrderCountMap);
        }
        return result;
    }

    /**
     * 计算总佣金收入、今日佣金收入、同日比和同周比
     */
    private void calculateAdditionalMetrics(CollectVo collectVo, List<OrderBill> orderBillListDB) {
        // 总佣金收入
        BigDecimal totalCommissionIncome = orderBillListDB.stream()
                .map(orderBill -> orderBill.getRealRecyclerPlatformAmount().add(orderBill.getRealCustomerPlatformAmount()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        collectVo.setTotalCommissionIncome(totalCommissionIncome);

        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1);
        LocalDate startOfWeek = today.with(DayOfWeek.MONDAY);
        LocalDate startOfLastWeek = startOfWeek.minusWeeks(1);

        // 今日佣金收入
        BigDecimal todayCommissionIncome = calculateIncomeForDate(orderBillListDB, today);
        collectVo.setTodayCommissionIncome(todayCommissionIncome);

        // 昨日佣金收入
        BigDecimal yesterdayCommissionIncome = calculateIncomeForDate(orderBillListDB, yesterday);

        // 本周佣金收入
        BigDecimal thisWeekIncome = calculateIncomeForRange(orderBillListDB, startOfWeek, today);

        // 上周佣金收入
        BigDecimal lastWeekIncome = calculateIncomeForRange(orderBillListDB, startOfLastWeek, startOfWeek.minusDays(1));

        // 计算同日比和同周比
        collectVo.setIsodiurnalRatio(calculateGrowth(todayCommissionIncome, yesterdayCommissionIncome));
        collectVo.setSyncyclicRatio(calculateGrowth(thisWeekIncome, lastWeekIncome));
    }

    /**
     * 计算特定日期的佣金收入
     */
    private BigDecimal calculateIncomeForDate(List<OrderBill> orderBillListDB, LocalDate date) {
        return orderBillListDB.stream()
                .filter(bill -> convertToLocalDate(bill.getPayTime()).isEqual(date))
                .map(bill -> bill.getRealRecyclerPlatformAmount().add(bill.getRealCustomerPlatformAmount()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * 计算特定日期范围内的佣金收入
     */
    private BigDecimal calculateIncomeForRange(List<OrderBill> orderBillListDB, LocalDate start, LocalDate end) {
        return orderBillListDB.stream()
                .filter(bill -> {
                    LocalDate payDate = convertToLocalDate(bill.getPayTime());
                    return (payDate.isEqual(start) || payDate.isAfter(start)) && payDate.isBefore(end.plusDays(1));
                })
                .map(bill -> bill.getRealRecyclerPlatformAmount().add(bill.getRealCustomerPlatformAmount()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * 计算特定日期范围内的订单量
     */
    private Long calculateOrderCountForRange(List<OrderInfo> orderBillListDB, LocalDate start, LocalDate end) {
        return orderBillListDB.stream()
                .filter(order -> {
                    LocalDate date = convertToLocalDate(order.getCreateTime());
                    return (date.isEqual(start) || date.isAfter(start)) && date.isBefore(end.plusDays(1));
                }).count();
    }

    /**
     * 计算特定日期范围内的订单支付量
     */
    private Long calculateOrderPayCountForRange(List<OrderBill> orderBillListDB, LocalDate start, LocalDate end) {
        return orderBillListDB.stream()
                .filter(order -> {
                    LocalDate payDate = convertToLocalDate(order.getPayTime());
                    return (payDate.isEqual(start) || payDate.isAfter(start)) && payDate.isBefore(end.plusDays(1));
                }).count();
    }

    /**
     * 将 Date 转换为 LocalDate
     */
    private LocalDate convertToLocalDate(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    /**
     * 将 Date 转换为 LocalDateTime
     */
    private LocalDateTime convertToLocalDateTime(Date date) {
        return date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }

    /**
     * 计算增长率（如同日比或同周比）
     */
    private BigDecimal calculateGrowth(BigDecimal current, BigDecimal previous) {
        if (previous.compareTo(BigDecimal.ZERO) == 0) {
            return current.compareTo(BigDecimal.ZERO) == 0 ? BigDecimal.ZERO : BigDecimal.valueOf(100);
        }
        return current.subtract(previous)
                .divide(previous, 4, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100));
    }

    /**
     * 计算两个 Date 类型时间的分钟差
     *
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @return 两个时间之间的分钟差(开始时间 > 结束时间 ： 返回负数)
     */
    private static long calculateMinutesDifference(Date startDate, Date endDate) {
        Instant startInstant = startDate.toInstant();
        Instant endInstant = endDate.toInstant();
        return ChronoUnit.MINUTES.between(startInstant, endInstant);
    }

    /**
     * 计算回收员超时费用
     *
     * @param calculateActualOrderVo
     * @param timeOutMin
     */
    private void calculateTimeoutFree(CalculateActualOrderVo calculateActualOrderVo, int timeOutMin) {
        OvertimeRequestForm overtimeRequestForm = new OvertimeRequestForm();
        overtimeRequestForm.setOvertimeMinutes(timeOutMin);
        OvertimeResponseVo overtimeResponseVo = serviceFeeRuleFeignClient.calculateTimeoutFree(overtimeRequestForm).getData();
        calculateActualOrderVo.setRecyclerOvertimeCharges(overtimeResponseVo.getOvertimeFee());
    }

    /**
     * 计算订单实际回收总金额、回收员实际所付款手续费、顾客实际所付款手续费
     *
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
     *
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
