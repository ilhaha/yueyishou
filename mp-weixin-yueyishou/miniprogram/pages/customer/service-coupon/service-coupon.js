import {
  reqCouponList
} from '../../../api/customer/coupon'
Page({
  data: {
    coupons: []
  },
  // 初始化数据
  onLoad() {
    this.initCouponList()
  },

  // 初始化服务抵扣劵列表
  async initCouponList() {
    const res = await reqCouponList();
    this.setData({
      coupons: res.data
    })
  },
  // 使用服务抵扣劵
  useCoupon(e) {
    wx.navigateTo({
      url: '/pages/category/category',
    })
  }
});