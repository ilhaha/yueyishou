import {
  reqAddressList,
  reqDelAddress
} from '../../../../api/customer/address'

import {
  swipeCellBehavior
} from '../../../../behavior/swipeCellBahavior'
Page({
  behaviors: [swipeCellBehavior],
  // 页面的初始数据
  data: {
    addressList: []
  },
  // 删除地址信息
  async deleteAddress(event) {
    const {
      id
    } = event.target.dataset
    console.log();
    await reqDelAddress(id)
    this.getAddressList()
  },

  // 获取当前登录的顾客地址信息
  async getAddressList() {
    const {
      data: addressList
    } = await reqAddressList();
    this.setData({
      addressList
    })
  },

  onShow() {
    this.getAddressList()
  },

  // 去编辑页面
  toEdit(event) {
    const {
      id
    } = event.currentTarget.dataset;
    wx.navigateTo({
      url: `../addorupdate/index?id=${id}`
    })
  }
})