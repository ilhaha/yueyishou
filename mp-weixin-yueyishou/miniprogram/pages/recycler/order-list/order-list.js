import {
  reqOrderListByStaus
} from '../../../api/recycler/order'
Page({
  data: {
    tabList: [{
      title: '待服务',
      status: 2
    }, {
      title: '服务中',
      status: 3
    }, {
      title: '待确认',
      status: 4
    }, {
      title: '待付款',
      status: 5
    }, {
      title: '待评价',
      status: 6
    }, {
      title: '已完成',
      status: 7
    }, {
      title: '已取消',
      status: 8
    }],
    active: 0,
    orderList: [],
  },
  onShow() {
    this.getOrderList(this.data.tabList[this.data.active].status);
  },
  // 查看订单详情
  showDetails(event) {
    const orderId = event.currentTarget.dataset.orderid;
    const orderstatus = event.currentTarget.dataset.orderstatus;
    wx.navigateTo({
      url: `/pages/recycler/order-details/order-details?orderId=${orderId}&orderStatus=${orderstatus}`,
    })
  },

  // 根据订单状态获取订单列表
  async getOrderList(status) {
    // 发送请求获取订单列表
    const res = await reqOrderListByStaus(status);
    // 设置订单数据
    this.setData({
      orderList: res.data
    });
  },

  // 切换订单列表
  switchList(event) {
    // 获取当前激活的 tab 索引
    const activeIndex = event.detail.index;

    // 设置当前激活的 tab
    this.setData({
      active: activeIndex
    });

    // 根据索引获取对应的 item
    const currentItem = this.data.tabList[activeIndex];
    // 获取 item 的 status
    const currentStatus = currentItem.status;

    // 获取对应状态的订单列表
    this.getOrderList(currentStatus);
  },
})