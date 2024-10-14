import {
  reqOrderDetails,
  reqCancelOrder
} from '../../../../api/customer/order'
Page({
  data: {
    orderInfo: null,
    orderStatus: 1,
  },
  onLoad(options) {
    this.getOrderInfo(options.orderId);
    this.setData({
      orderStatus: options.orderstatus
    })
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
  }
})