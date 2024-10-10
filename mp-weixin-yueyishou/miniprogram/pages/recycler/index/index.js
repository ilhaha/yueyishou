import {
  reqSwitchServiceStatus
} from '../../../api/recycler/personalization'

import {
  toast
} from '../../../utils/extendApi'
Page({
  data: {
    activeTab: 0,
    recyclerInfo: {},
    orderList: [],
    popupShow: false
  },
  // 初始化数据
  onLoad() {},

  // 切换回收员服务状态
  async switchServiceStatus(event) {
    await reqSwitchServiceStatus(event.detail ? 1 : 0);
    this.setData({
      'recyclerInfo.serviceStatus': event.detail ? 1 : 0
    })
  },
  // 开始接单
  async takeOrders() {
    const res = await reqSwitchServiceStatus(1)
    if (res.data) {
      toast({
        title: '开始服务',
        icon: 'success'
      })
      this.setData({
        'recyclerInfo.serviceStatus': 1
      })
    }
  },

  // 获取回收员信息
  getRecyclerInfo(e) {
    this.setData({
      recyclerInfo: e.detail.recyclerInfo
    })
  },

  // 切换tab页
  onTabChange(e) {
    const status = e.detail.status || 1;
    const index = e.detail.index;
    console.log(status);
    this.setData({
      activeTab: index
    });
  },

  // 点击头像按钮
  onLeftTap() {
    this.setData({
      popupShow: !this.data.popupShow
    })
  },
});