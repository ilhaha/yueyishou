import {
  reqRecyclerAccountInfo
} from '../../../api/recycler/accout'
Page({
  data: {
    showPicker: false, // 控制时间选择器弹窗的显示
    selectedDate: '', // 用户选择的日期
    hasIncome: false, // 控制是否显示收益内容
    currentDate: new Date().getTime(),
    maxDate: new Date().getTime(),
    accountInfo: {
      totalAmount: 0,
      totalRecyclePay: 0,
      recyclerAccountDetailVoList: []
    },
  },

  onShow() {
    this.loadTransactions(); // 加载交易明细数据
  },
  // 充值
  onRecharge() {
    wx.navigateTo({
      url: `../wallet-options/wallet-options?totalAmount=${this.data.accountInfo.totalAmount}&flag=2`,
    })
  },
  // 提现
  onWithdraw() {
    wx.navigateTo({
      url: `../wallet-options/wallet-options?totalAmount=${this.data.accountInfo.totalAmount}&flag=1`,
    })
  },
  // 加载交易明细数据
  async loadTransactions(deatilsTime = {}) {
    const {
      data
    } = await reqRecyclerAccountInfo(deatilsTime);
    console.log(data);
    this.setData({
      accountInfo: {
        totalAmount: data.totalAmount.toFixed(2),
        totalRecyclePay: data.totalRecyclePay.toFixed(2),
        recyclerAccountDetailVoList: data.recyclerAccountDetailVoList
      },
    })
  },

  // 打开时间选择器
  openPicker() {
    this.setData({
      showPicker: true
    });
  },

  // 关闭时间选择器
  closePicker() {
    this.setData({
      showPicker: false
    });
  },
  // 取消日期选择
  onCancel() {
    this.loadTransactions();
    this.setData({
      selectedDate: '',
      showPicker: false
    })
  },
  // 选择日期
  onInput(event) {
    const date = new Date(event.detail);
    const year = date.getFullYear();
    const month = (date.getMonth() + 1).toString().padStart(2, '0'); // 补零
    const selectedDate = `${year}-${month}`;
    this.loadTransactions({
      detailsTime: selectedDate
    });
    this.setData({
      selectedDate,
      showPicker: false
    });
  },
});