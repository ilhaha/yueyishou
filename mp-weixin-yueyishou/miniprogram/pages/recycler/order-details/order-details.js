import {
  reqOrderDetails,
  reqGrabOrder
} from '../../../api/recycler/order'
import {
  toast
} from '../../../utils/extendApi'

Page({
  data: {
    orderInfo: {},
    apart: 0
  },
  onLoad(options) {
    this.getOrderInfo(options.orderId);
    this.setData({
      apart: options.apart
    })
    console.log(this.data.orderInfo);
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
      orderInfo: res.data
    })
  }
})