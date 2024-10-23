import {
  reqCustomerAccountOnWithdraw
} from '../../../api/customer/customer'

Page({
  data: {
    totalAmount: 0.00, // 可提现金额
    inputAmount: '', // 输入的金额
  },

  // 初始化数据
  onLoad(options) {
    const {
      totalAmount
    } = options;
    this.setData({
      totalAmount
    });
  },

  // 处理输入金额
  onInputAmount(e) {
    this.setData({
      inputAmount: e.detail.value
    });
  },

  // 全部提现
  selectAll() {
    this.setData({
      inputAmount: this.data.totalAmount
    });
  },


  // 提现到微信
  async withdrawToWeChat() {
    const {
      inputAmount,
      totalAmount
    } = this.data;
    const amount = parseFloat(inputAmount);

    if (amount < 1) {
      wx.showToast({
        title: '提现金额不能少于 1 元',
        icon: 'none'
      });
      return;
    }

    if (amount > totalAmount) {
      wx.showToast({
        title: '余额不足',
        icon: 'none'
      });
      return;
    }

    const res = await reqCustomerAccountOnWithdraw({
      amount
    })
    if (res.data) {
      wx.showToast({
        title: '提现申请成功',
        icon: 'success'
      });
      setTimeout(() => {
        this.goBack();
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
});