import {
  reqOrderDetails,
  reqGrabOrder,
  reqRepostOrder,
  reqRecyclerArrive,
  reqCalculateActual,
  reqValidateRecycleCode,
  reqSettlement,
  reqCalculateCancellationFee,
  reqCancelOrderAfterTaking,
  reqRejectOrder,
  reqGetRejectInfo,
  reqCancelRejectOrder,
  reqRejectFeedback
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
import {
  reqCosUpload
} from '../../../api/recycler/cos'

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
    settlementShow: false,
    customerLateCancellationFee: null,
    overtimeMinutes: null,
    recyclerLateCancellationFee: null,
    serviceOvertimePenalty: null,
    dialogRejectShow: false,
    rejectReason: '',
    fileList: [],
    beforeCloseDialogReject: {},
    dialogRejectInfoShow: false,
    rejectFileList: [],
    rejectMessage: '',
    cancelRejectShow: false,
    reasonModel: false,
    reason: '',
    proofList: []
  },
  onLoad(options) {
    this.getOrderInfo(options.orderId);
    this.setData({
      orderStatus: options.orderStatus,
      beforeCloseDialogReject: (action) => new Promise((resolve, reject) => {
        setTimeout(() => {
          // 拦截确认操作
          resolve(false);
        }, 0);
      })
    })
  },
  confirm() {
    this.setData({
      reasonModel: false
    })
  },
  // 查看审核不通过原因
  async showReason() {
    const res = await reqRejectFeedback(this.data.orderInfo.id);
    this.setData({
      reasonModel: true,
      reason: res.data.reason,
      proofList: res.data.proof.split(',')
    })
  },
  // 查询提交拒收申请
  restApply() {
    this.switchDialogRejectInfoShow();
    this.switchDialogRejectShow();
  },
  // 取消申请拒收订单
  async cancelApply() {
    const res = await reqCancelRejectOrder(this.data.orderInfo.id);
    if (res.data) {
      this.getOrderInfo(this.data.orderInfo.id);
      this.switchDialogRejectInfoShow();
      toast({
        title: "已取消",
        icon: 'success',
        duration: 1000
      })
    }
  },
  // 切换确定是否取消申请拒收订单弹窗状态
  switchCancelRejectShow() {
    this.setData({
      cancelRejectShow: !this.data.cancelRejectShow
    })
  },
  // 切换查看订单拒收信息弹窗
  async switchDialogRejectInfoShow() {
    this.setData({
      dialogRejectInfoShow: !this.data.dialogRejectInfoShow
    })
    // 打开弹窗，发送请求获取拒收信息
    if (this.data.dialogRejectInfoShow) {
      const res = await reqGetRejectInfo(this.data.orderInfo.id);
      this.setData({
        rejectFileList: res.data.rejectActualPhotos.split(","),
        rejectMessage: res.data.cancelMessage
      });
    }
  },
  // 获取拒收内容
  getRejectReason(event) {
    this.setData({
      rejectReason: event.detail
    });
  },
  // 申请拒收订单
  async rejectOrder() {
    const {
      fileList,
      rejectReason
    } = this.data;

    if (!rejectReason) {
      wx.showToast({
        title: '请填写拒收理由',
        icon: 'none'
      });
      return true;
    }
    if (fileList.length === 0) {
      wx.showToast({
        title: '请上传至少一张回收物的照片',
        icon: 'none'
      });
      return true;
    }
    const res = await reqRejectOrder({
      orderId: this.data.orderInfo.id,
      rejectActualPhotos: fileList.map(file => file.url).join(','),
      cancelMessage: rejectReason,
      acceptTime: this.data.orderInfo.acceptTime,
      arriveTime: this.data.orderInfo.arriveTime
    });
    if (res.data) {
      this.getOrderInfo(this.data.orderInfo.id);
      this.switchDialogRejectShow();
      toast({
        title: "已申请",
        icon: 'success',
        duration: 1000
      })
    }
  },
  // 切换拒收订单申请框的状态
  switchDialogRejectShow() {
    this.setData({
      dialogRejectShow: !this.data.dialogRejectShow
    })
  },
  // 上传图片
  async reqCosUpload(event) {
    const res = await reqCosUpload(event, 'order');
    // 构造新的文件对象
    let file = {
      url: res.data.url
    }
    // 使用扩展运算符生成新的 fileList 数组
    this.setData({
      fileList: [...this.data.fileList, file] // 扩展原数组并添加新文件
    })

  },
  // 处理删除图片
  onDelete(event) {
    const index = event.detail.index; // 获取被删除的图片索引
    const newFileList = [...this.data.fileList]; // 复制当前的 fileList
    newFileList.splice(index, 1); // 删除对应索引的图片

    this.setData({
      fileList: newFileList // 更新 fileList
    });
  },
  // 接单后回收员取消订单
  async recyclercancelOrder(event) {
    if (!this.data.serviceOvertimePenalty && !this.data.customerLateCancellationFee && !this.data.recyclerLateCancellationFee) {
      const resp = await reqRepostOrder(this.data.orderInfo.id);
      if (resp.data) {
        toast({
          title: '已取消',
          icon: 'susscess'
        })
        setTimeout(() => {
          this.goBack()
        }, 1000);
      }
      return;
    };
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
        icon: 'susscess'
      })
      setTimeout(() => {
        this.goBack()
      }, 1000);
    }
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
  // 查看回收员接单后，取消订单费用
  async initCancelFee() {
    const {
      orderInfo
    } = this.data
    const res = await reqCalculateCancellationFee({
      appointmentTime: orderInfo.appointmentTime,
      acceptTime: orderInfo.acceptTime,
      cancelOperator: 'recycler'
    });
    this.setData({
      customerLateCancellationFee: res.data.customerLateCancellationFee,
      overtimeMinutes: res.data.overtimeMinutes,
      recyclerLateCancellationFee: res.data.recyclerLateCancellationFee,
      serviceOvertimePenalty: res.data.serviceOvertimePenalty,
    })
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
    // 查看回收员接单后，取消费用
    this.initCancelFee();
  }
})