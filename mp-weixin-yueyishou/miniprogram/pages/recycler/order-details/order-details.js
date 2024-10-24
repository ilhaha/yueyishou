import {
  reqOrderDetails,
  reqGrabOrder,
  reqRepostOrder,
  reqRecyclerArrive,
  reqCalculateActual,
  reqValidateRecycleCode,
  reqSettlement
} from '../../../api/recycler/order'
import {
  toast
} from '../../../utils/extendApi'
import {
  timeBehavior
} from '../../../behavior/timeBehavior'
import {
  reqGenerateBill
} from '../../../api/recycler/bill'

Page({
  behaviors: [timeBehavior],
  data: {
    orderInfo: null,
    orderStatus: 1,
    freeCancellationTime: null,
    dialogShow: false,
    actualOrderInfo: {},
    selectedCoupon: null,
    couponListWithNone: [],
    initServiceFee: 0,
    generateBillForm: {
      orderId: null,
      couponId: null,
      realOrderRecycleAmount: 0.00,
      realRecyclerAmount: 0.00,
      realRecyclerPlatformAmount: 0.00,
      recyclerOvertimeCharges: 0.00,
      recyclerCouponAmount: 0.00,
      realCustomerPlatformAmount: 0.00,
    },
    settlementShow: false
  },
  onLoad(options) {
    this.getOrderInfo(options.orderId);
    this.setData({
      orderStatus: options.orderStatus
    })
  },

  // 结算付款
  async settlement(event) {
    const res = await reqSettlement({
      orderId: this.data.orderInfo.id
    });
    if (res.data) {
      toast({
        title: '结算成功',
        icon: 'success'
      })
      setTimeout(() => {
        this.goBack()
      }, 1000);
    }
  },

  // 切换结算弹窗
  switchSettlementShow() {
    this.setData({
      settlementShow: !this.data.settlementShow
    })
  },

  // 校验回收码
  async validateRecycleCode() {
    const {
      inputRecycleCode
    } = this.data;

    // 正则表达式检查是否是6位数字
    const isValidCode = /^[0-9]{6}$/.test(inputRecycleCode);

    if (!isValidCode) {
      wx.showToast({
        title: '请输入6位数字的回收码',
        icon: 'none'
      });
      return; // 不符合格式直接返回
    }

    inputRecycleCode
    const res = await reqValidateRecycleCode({
      orderId: this.data.orderInfo.id,
      recycleCode: inputRecycleCode
    })
    // 校验回收码是否正确
    if (res.data) {
      wx.showToast({
        title: '回收码校验成功！',
        icon: 'success'
      });
      setTimeout(() => {
        this.goBack()
      }, 1000);
    } else {
      wx.showToast({
        title: '回收码错误',
        icon: 'error'
      });
    }
  },

  // 输入回收码
  onRecycleCodeInput(e) {
    this.setData({
      inputRecycleCode: e.detail.value
    });
  },

  // 阻止输入回收码时事件冒泡
  stopTapPropagation(e) {
    // 阻止 tap 事件冒泡到父元素
    e.stopPropagation();
  },

  // 回收员选择服务抵扣劵
  onCouponChange(e) {
    const index = e.detail.value;
    const selectedCoupon = this.data.couponListWithNone[index];

    // 初始化数据
    let serviceFee = this.data.initServiceFee; // 使用初始服务费
    let realRecyclerAmount = this.data.actualOrderInfo.realRecyclerAmount; // 回收总金额

    if (selectedCoupon.id === 0) {
      // 不使用优惠券，恢复原始总金额和服务费
      const totalAmount = (serviceFee + realRecyclerAmount + (this.data.actualOrderInfo.recyclerOvertimeCharges ? this.data.actualOrderInfo.recyclerOvertimeCharges : 0)).toFixed(2); // 保留两位小数

      this.setData({
        generateBillForm: {
          recyclerCouponAmount: 0
        },
        selectedCoupon: null,
        actualOrderInfo: {
          ...this.data.actualOrderInfo,
          totalAmount: totalAmount,
          realRecyclerPlatformAmount: serviceFee.toFixed(2) // 保留两位小数
        }
      });
    } else {
      // 使用打折抵扣券或免单抵扣券
      if (selectedCoupon.couponType == 1) {
        // 计算折扣后的服务费
        serviceFee = (this.data.initServiceFee * (selectedCoupon.discount / 10)).toFixed(2);
        this.setData({
          generateBillForm: {
            recyclerCouponAmount: (this.data.initServiceFee - serviceFee).toFixed(2)
          }
        })
      } else {
        this.setData({
          generateBillForm: {
            recyclerCouponAmount: serviceFee
          }
        })
        serviceFee = (0).toFixed(2); // 免单服务券，服务费为0
      }
      // 计算最终总金额
      const finalAmount = (parseFloat(realRecyclerAmount) + parseFloat(serviceFee) + parseFloat(this.data.actualOrderInfo.recyclerOvertimeCharges ? this.data.actualOrderInfo.recyclerOvertimeCharges : 0)).toFixed(2);

      this.setData({
        selectedCoupon: selectedCoupon,
        actualOrderInfo: {
          ...this.data.actualOrderInfo,
          totalAmount: finalAmount,
          realRecyclerPlatformAmount: serviceFee
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
      this.setData({
        selectedCoupon: null
      })
    }
  },
  // 确认账单信息
  async confirmBillInfo() {
    const res = await reqCalculateActual(this.data.orderInfo.id);
    const couponListWithNone = [{
      id: 0,
      name: "不选择优惠券",
      discount: 1
    }].concat(res.data.recyclerAvailableCouponList);
    this.setData({
      actualOrderInfo: res.data,
      couponListWithNone,
      initServiceFee: res.data.realRecyclerPlatformAmount,
    })
    this.switchDialogStatus();
  },

  // 确认回收 
  async confirmRecycling() {
    const {
      realRecyclerAmount,
      realRecyclerPlatformAmount,
      recyclerOvertimeCharges,
      totalAmount,
      realCustomerPlatformAmount
    } = this.data.actualOrderInfo;
    this.setData({
      generateBillForm: {
        ...this.data.generateBillForm,
        orderId: this.data.orderInfo.id,
        couponId: this.data.selectedCoupon ? this.data.selectedCoupon.id : null,
        realOrderRecycleAmount: realRecyclerAmount,
        realRecyclerAmount: totalAmount,
        realRecyclerPlatformAmount: realRecyclerPlatformAmount,
        recyclerOvertimeCharges: recyclerOvertimeCharges,
        realCustomerPlatformAmount: realCustomerPlatformAmount,
      }
    })
    const res = await reqGenerateBill(this.data.generateBillForm);
    if (res.data) {
      toast({
        title: '已确认',
        icon: 'success',
        duration: 1000
      })
      setTimeout(() => {
        this.goBack()
      }, 1000);
    }
  },
  // 跳转更新订单信息页面
  updateOrder() {
    wx.navigateTo({
      url: `/pages/recycler/order-update/order-update?orderInfo=${encodeURIComponent(JSON.stringify(this.data.orderInfo))}`
    });
  },
  // 回收员到达回收点
  async recyclerArriveOrder() {
    const res = await reqRecyclerArrive(this.data.orderInfo.id);
    if (res.data) {
      this.goBack();
    } else {
      toast({
        title: '未成功',
        icon: 'error'
      })
    }

  },
  // 接单之后回收员取消订单
  async recyclerCanceOrder() {
    // 判断当前时间是否大于预约上门时间，如果大于则需要回收员支付
    if (this.calculateTimeDifference(this.data.orderInfo.appointmentTime) > 0) {
      toast({
        title: "回收员迟到，后续处理"
      })
      return;
    }
    // 如果当前时间距离预约上门时间还有60分钟，则免费取消
    const diffMin = this.calculateTimeDifference(this.data.orderInfo.appointmentTime);
    if (diffMin > -60) {
      toast({
        title: "回收员付费取消"
      })
    } else {
      // 取消接单，把订单查询发给符合接单的回收员
      await reqRepostOrder(this.data.orderInfo.id)
      toast({
        title: "取消成功",
        duration: 1000,
        icon: 'success'
      })
      setTimeout(() => {
        this.goBack()
      }, 1000);
    }
  },
  // 返回上个页面，并且调用onShow方法
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
  // 抢单
  async grabOrder(event) {
    const res = await reqGrabOrder(event.currentTarget.dataset.orderid);
    toast({
      title: res.data ? '抢单成功' : '该单已被抢',
      icon: res.data ? 'success' : 'error',
      duration: 1000
    })
    setTimeout(() => {
      wx.navigateBack();
    }, 1000);

  },
  // 获取订单详情
  async getOrderInfo(orderId) {
    const res = await reqOrderDetails(orderId);
    this.setData({
      orderInfo: res.data,
      freeCancellationTime: this.calculatePastTime(res.data.appointmentTime, 60)
    })
  }
})