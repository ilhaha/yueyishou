import {
  reqOrderDetails,
  reqCancelOrder,
  reqReview,
  reqCalculateCancellationFee,
  reqCancelOrderAfterTaking
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
    recycleCode: '',
    isReviewDialogShow: false, // 控制评价对话框显示状态
    reviewContent: "", // 用户的评价内容
    rating: 0, // 用户评分
    beforeClose: {},
    customerLateCancellationFee: null,
    overtimeMinutes: null,
    recyclerLateCancellationFee: null,
    serviceOvertimePenalty: null,
  },
  onLoad(options) {
    this.getOrderInfo(options.orderId);
    this.setData({
      orderStatus: options.orderstatus,
      beforeClose: (action) => new Promise((resolve, reject) => {
        setTimeout(() => {
          // 拦截确认操作
          resolve(false);
        }, 0);
      })
    })
  },

  // 查看回收员接单后，取消订单费用
  async initCancelFee() {
    const {
      orderInfo
    } = this.data
    const res = await reqCalculateCancellationFee({
      appointmentTime: orderInfo.appointmentTime,
      acceptTime: orderInfo.acceptTime,
      cancelOperator: 'customer'
    });
    this.setData({
      customerLateCancellationFee: res.data.customerLateCancellationFee,
      overtimeMinutes: res.data.overtimeMinutes,
      recyclerLateCancellationFee: res.data.recyclerLateCancellationFee,
      serviceOvertimePenalty: res.data.serviceOvertimePenalty,
    })
  },

  // 提交评价
  async submitReview() {
    const {
      reviewContent,
      rating,
      orderInfo
    } = this.data;
    if (!reviewContent || rating === 0) {
      wx.showToast({
        title: '请填写完整评价内容',
        icon: 'none'
      });
      return;
    }
    let data = {
      orderId: orderInfo.id,
      recyclerId: orderInfo.recyclerId,
      customerId: orderInfo.customerId,
      rate: rating,
      reviewContent: reviewContent
    }
    const res = await reqReview(data);
    if (res.data) {
      toast({
        title: '评价成功',
        icon: 'success'
      })
      setTimeout(() => {
        wx.navigateBack({
          delta: 1, // 返回到上一个页面
          success: function () {
            const pages = getCurrentPages(); // 获取当前页面栈
            const prevPage = pages[pages.length - 2]; // 上一个页面
            if (prevPage) {
              // 可以手动调用 prevPage 的方法，或重新设置数据
              prevPage.onShow(); // 手动调用上一个页面的 onShow 方法
            }
          }
        });
      }, 1000);
    }
  },
  // 输入评价内容
  onReviewInput(e) {
    this.setData({
      reviewContent: e.detail
    })
  },
  // 评分
  onChange(event) {
    this.setData({
      rating: event.detail
    });
  },

  // 切换评价对话框状态
  swtichReviewDialog() {
    this.setData({
      isReviewDialogShow: !this.data.isReviewDialogShow
    });
    if (!this.data.isReviewDialogShow) {
      this.setData({
        reviewContent: '',
        rating: 0
      })
    }
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
      recycleCodeShow: !this.data.recycleCodeShow,
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
  async customercancelOrder(event) {
    if (!this.data.serviceOvertimePenalty && !this.data.customerLateCancellationFee && !this.data.recyclerLateCancellationFee) {
      this.cancelOrder();
      return;
    }
    const res = await reqCancelOrderAfterTaking({
      orderId: this.data.orderInfo.id,
      customerId: this.data.orderInfo.customerId,
      recyclerId: this.data.orderInfo.recyclerId,
      serviceOvertimePenalty: this.data.serviceOvertimePenalty,
      customerLateCancellationFee: this.data.customerLateCancellationFee,
      recyclerLateCancellationFee: this.data.recyclerLateCancellationFee
    })
    if (res.data) {
      toast({
        title: '已取消',
        icon: "success"
      })
      setTimeout(() => {
        this.goBack()
      }, 1000);
    }
  },
  // 返回上个页面
  goBack() {
    wx.navigateBack({
      delta: 1, // 返回到上一个页面
      success: function () {
        const pages = getCurrentPages(); // 获取当前页面栈
        const prevPage = pages[pages.length - 2]; // 上一个页面
        if (prevPage) {
          // 可以手动调用 prevPage 的方法，或重新设置数据
          prevPage.onShow(); // 手动调用上一个页面的 onShow 方法
        }
      }
    });
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
        couponId: null,
        realCustomerRecycleAmount: res.data.realOrderRecycleAmount - res.data.realCustomerPlatformAmount + (res.data.recyclerOvertimeCharges || 0),
        realCustomerPlatformAmount: res.data.realCustomerPlatformAmount,
        customerCouponAmount: 0.00,
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
      // 查看回收员接单后，取消费用
      this.initCancelFee();
    }

  },
})