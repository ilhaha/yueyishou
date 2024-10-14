import {
  reqOrderDetails,
  reqCancelOrder
} from '../../../../api/customer/order'
import {
  toast
} from '../../../../utils/extendApi'
import {
  timeBehavior
} from '../../../../behavior/timeBehavior'

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
      orderStatus: options.orderstatus
    })
  },
  // 回收员接单后，用户取消订单
  customerCanceOrder(event) {
    // 判断当前时间是否大于预约上门时间，如果大于则需要回收员支付
    if (this.calculateTimeDifference(this.data.orderInfo.appointmentTime) < 0) {
      toast({
        title: "回收员迟到，后续处理"
      })
      return;
    }
    const diffMin = this.calculateTimeDifference(this.data.orderInfo.acceptTime);
    if (diffMin > 5) {
      toast({
        title: "大于五分钟，后续处理"
      })
    } else {
      // 取消订单
      // this.cancelOrder();
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
    this.setData({
      orderInfo: res.data
    })
    // 更新到 data
    this.setData({
      freeCancellationTime: this.calculateFutureTime(this.data.orderInfo.acceptTime, 5)
    });
  },
})