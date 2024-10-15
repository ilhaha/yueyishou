import {
  reqOrderDetails,
  reqGrabOrder
} from '../../../api/recycler/order'
import {
  toast
} from '../../../utils/extendApi'
import {
  timeBehavior
} from '../../../behavior/timeBehavior'

Page({
  behaviors: [timeBehavior],
  data: {
    orderInfo: null,
    orderStatus: 1,
    freeCancellationTime: null
  },
  onLoad(options) {
    this.getOrderInfo(options.orderId);
    this.setData({
      orderStatus: options.orderStatus
    })
  },
  // 接单之后回收员取消订单
  recyclerCanceOrder() {
    // 判断当前时间是否大于预约上门时间，如果大于则需要回收员支付
    if (this.calculateTimeDifference(this.data.orderInfo.appointmentTime) > 0) {
      toast({
        title: "回收员迟到，后续处理"
      })
      return;
    }
    // 如果当前时间距离预约上门时间还有60分钟，则免费取消
    const diffMin = this.calculateTimeDifference(this.data.orderInfo.appointmentTime);
    console.log(this.data.orderInfo.appointmentTime);
    console.log(diffMin);
    if (diffMin > -60) {
      toast({
        title: "回收员付费取消"
      })
    } else {
      toast({
        title: "回收员免费取消"
      })
    }
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