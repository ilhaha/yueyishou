import {
  reqOrderDetails
} from '../../../../api/customer/order'
Page({
  data: {
    orderInfo: {}
  },
  onLoad(options) {
    this.getOrderInfo(options.orderId);
  },
  // 获取订单详情
  async getOrderInfo(orderId) {
    const res = await reqOrderDetails(orderId);
    this.setData({
      orderInfo: res.data
    })
    console.log(this.data.orderInfo);
  }
})