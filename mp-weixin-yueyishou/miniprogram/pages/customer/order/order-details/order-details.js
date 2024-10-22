import {
  reqOrderDetails,
  reqCancelOrder
} from '../../../../api/customer/order'
import {
  toast
} from '../../../../utils/extendApi'
import {
  timeBehavior
} from '../../../../behavior/timeBehavior';
import {
  reqUpdateBill
} from '../../../../api/customer/bill'

Page({
  behaviors: [timeBehavior],
  data: {
    orderInfo: null,
    orderStatus: 1,
    freeCancellationTime: null,
    dialogShow: false,
    couponListWithNone: [],
    initServiceFee: 0,
    selectedCoupon: null,
    totalAmount: '',
    customerCouponAmount: 0,
    updateBillForm: {
      orderId: null,
      couponId: null,
      realCustomerRecycleAmount: 0.00,
      realCustomerPlatformAmount: 0.00,
      customerCouponAmount: 0.00
    },
    recycleCodeShow: false,
    recycleCode: ''
  },
  onLoad(options) {
    this.getOrderInfo(options.orderId);
    this.setData({
      orderStatus: options.orderstatus
    })
  },
  // 提醒付款
  tipPay() {
    toast({
      title: '已提醒',
      icon: 'success'
    })
  },
  // 查看回收码
  showRecycleCode() {
    this.setData({
      recycleCodeShow: !this.data.recycleCodeShow
    })
  },
  // 顾客确认回收
  async confirmRecycling() {
    const res = await reqUpdateBill(this.data.updateBillForm);
    if (res.data) {
      this.getOrderInfo(this.data.orderInfo.id);
      this.setData({
        recycleCode: res.data
      })
      toast({
        title: '已确认',
        icon: 'success',
        duration: 1000
      })
    }
  },
  // 顾客选择服务抵扣劵
  onCouponChange(e) {
    const index = e.detail.value;
    const selectedCoupon = this.data.couponListWithNone[index];

    // 初始化数据
    let serviceFee = this.data.initServiceFee;

    let realOrderRecycleAmount = this.data.orderInfo.realOrderRecycleAmount;

    if (selectedCoupon.id === 0) {
      // 不使用优惠券，恢复原始总金额和服务费
      const totalAmount = (realOrderRecycleAmount - serviceFee + (this.data.orderInfo.recyclerOvertimeCharges ? this.data.orderInfo.recyclerOvertimeCharges : 0)).toFixed(2); // 保留两位小数
      this.setData({
        updateBillForm: {
          ...this.data.updateBillForm,
          couponId: null,
          realCustomerRecycleAmount: totalAmount,
          realCustomerPlatformAmount: serviceFee,
          customerCouponAmount: 0
        },
        customerCouponAmount: 0,
        selectedCoupon: null,
        totalAmount: totalAmount * 100,
        orderInfo: {
          ...this.data.orderInfo,
          realCustomerPlatformAmount: serviceFee.toFixed(2) // 保留两位小数
        }
      });
    } else {
      // 使用打折抵扣券或免单抵扣券
      if (selectedCoupon.couponType == 1) {
        // 计算折扣后的服务费
        serviceFee = (this.data.initServiceFee * (selectedCoupon.discount / 10)).toFixed(2);
        this.setData({
          customerCouponAmount: (this.data.initServiceFee - serviceFee).toFixed(2)
        })
      } else {
        this.setData({
          customerCouponAmount: serviceFee
        })
        serviceFee = (0).toFixed(2); // 免单服务券，服务费为0
      }
      // 计算最终总金额
      const finalAmount = (parseFloat(realOrderRecycleAmount) - parseFloat(serviceFee) + parseFloat(this.data.orderInfo.recyclerOvertimeCharges ? this.data.orderInfo.recyclerOvertimeCharges : 0)).toFixed(2);
      this.setData({
        updateBillForm: {
          ...this.data.updateBillForm,
          couponId: selectedCoupon.id ? selectedCoupon.id : null,
          realCustomerRecycleAmount: finalAmount,
          realCustomerPlatformAmount: serviceFee,
          customerCouponAmount: this.data.customerCouponAmount
        },
        selectedCoupon: selectedCoupon,
        totalAmount: finalAmount * 100,
        orderInfo: {
          ...this.data.orderInfo,
          realCustomerPlatformAmount: serviceFee
        }
      });
    }
  },
  // 切换Dialog框状态
  switchDialogStatus() {
    this.setData({
      dialogShow: !this.data.dialogShow
    })

    if (!this.data.dialogShow) {
      // 初始化数据
      let serviceFee = this.data.initServiceFee;

      let realOrderRecycleAmount = this.data.orderInfo.realOrderRecycleAmount;
      // 不使用优惠券，恢复原始总金额和服务费
      const totalAmount = (realOrderRecycleAmount - serviceFee + (this.data.orderInfo.recyclerOvertimeCharges ? this.data.orderInfo.recyclerOvertimeCharges : 0)).toFixed(2); // 保留两位小数
      this.setData({
        updateBillForm: {
          ...this.data.updateBillForm,
          couponId: null,
          realCustomerRecycleAmount: totalAmount,
          realCustomerPlatformAmount: serviceFee,
          customerCouponAmount: 0
        },
        customerCouponAmount: 0,
        selectedCoupon: null,
        totalAmount: totalAmount * 100,
        orderInfo: {
          ...this.data.orderInfo,
          realCustomerPlatformAmount: serviceFee.toFixed(2) // 保留两位小数
        }
      });
    }
  },

  // 回收员接单后，用户取消订单
  customerCanceOrder(event) {
    // 判断当前时间是否大于预约上门时间，如果大于则需要回收员支付
    if (this.calculateTimeDifference(this.data.orderInfo.appointmentTime) > 0) {
      toast({
        title: "回收员迟到，后续处理"
      })
      return;
    }
    const diffMin = this.calculateTimeDifference(this.data.orderInfo.acceptTime);
    if (diffMin > 5) {
      toast({
        title: "大于五分钟，付费取消"
      })
    } else {
      // 取消订单
      this.cancelOrder();
    }
  },

  // 取消订单
  async cancelOrder(event) {
    await reqCancelOrder(this.data.orderInfo.id);
    wx.switchTab({
      url: '/pages/customer/order/order',
    })
  },

  // 获取订单详情
  async getOrderInfo(orderId) {
    const res = await reqOrderDetails(orderId);
    const couponListWithNone = [{
      id: 0,
      name: "不选择优惠券",
      discount: 1
    }].concat(res.data.availableCouponVoList);
    this.setData({
      updateBillForm: {
        ...this.data.updateBillForm,
        orderId: res.data.id
      },
      recycleCode: res.data.recycleCode,
      orderInfo: res.data,
      totalAmount: (res.data.realOrderRecycleAmount - res.data.realCustomerPlatformAmount + (res.data.recyclerOvertimeCharges ? res.data.recyclerOvertimeCharges : 0)) * 100,
      couponListWithNone,
      initServiceFee: res.data.realCustomerPlatformAmount
    })
    if (this.data.orderStatus == 2) {
      this.setData({
        freeCancellationTime: this.calculateFutureTime(res.data.acceptTime, 5)
      })
    }

  },
})