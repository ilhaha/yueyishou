import {
  reqCategoryParent,
  reqSubCategories
} from '../../api/customer/category'

import {
  toast
} from '../../utils/extendApi'

Page({
  data: {
    categoryParentList: []
  },
  // 选择废品父品类，跳转下单页面
  async placeOrder(options) {
    const {
      data
    } = await reqSubCategories(options.currentTarget.dataset.parentid);
    if (data == null || data.length <= 0) {
      toast({
        title: '改该功能建设中',
        duration: 500
      })
      return;
    }
    wx.navigateTo({
      url: `/pages/place-order/place-order?sub=${encodeURIComponent(JSON.stringify(data))}`,
    })
  },
  // 获取废品父品类
  async getCategoryParentList() {
    const res = await reqCategoryParent();
    this.setData({
      categoryParentList: res.data
    })
  },
  onLoad() {
    this.getCategoryParentList();
  }
})