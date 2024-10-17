import {
  reqOrderDetails,
  reqGrabOrder,
  reqRepostOrder,
  reqRecyclerArrive
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
  updateOrder() {
    console.log(this.data.orderInfo.id);
  },
  // 回收员到达回收点
  async recyclerArriveOrder() {
    const res = await reqRecyclerArrive(this.data.orderInfo.id);
    if (res.data) {
      this.goBack();
    } else {
      toast({
        title: '未成功',
        icon: 'error'
      })
    }

  },
  // 接单之后回收员取消订单
  async recyclerCanceOrder() {
    // 判断当前时间是否大于预约上门时间，如果大于则需要回收员支付
    if (this.calculateTimeDifference(this.data.orderInfo.appointmentTime) > 0) {
      toast({
        title: "回收员迟到，后续处理"
      })
      return;
    }
    // 如果当前时间距离预约上门时间还有60分钟，则免费取消
    const diffMin = this.calculateTimeDifference(this.data.orderInfo.appointmentTime);
    if (diffMin > -60) {
      toast({
        title: "回收员付费取消"
      })
    } else {
      // 取消接单，把订单查询发给符合接单的回收员
      await reqRepostOrder(this.data.orderInfo.id)
      toast({
        title: "取消成功",
        duration: 1000,
        icon: 'success'
      })
      setTimeout(() => {
        this.goBack()
      }, 1000);
    }
  },
  // 返回上个页面，并且调用onShow方法
  goBack() {
    wx.navigateBack({
      delta: 1, // 返回到上一个页面
      success: function () {
        const pages = getCurrentPages(); // 获取当前页面栈
        const prevPage = pages[pages.length - 2]; // 上一个页面
        if (prevPage) {
          // 可以手动调用 prevPage 的方法，或重新设置数据
          prevPage.onShow(); // 手动调用上一个页面的 onShow 方法
        }
      }
    });
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