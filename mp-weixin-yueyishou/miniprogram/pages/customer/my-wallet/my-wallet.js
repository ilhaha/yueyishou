import {
  reqCustomerAccountInfo
} from '../../../api/customer/account'
Page({
  data: {
    showPicker: false, 
    selectedDate: '',
    hasIncome: false,
    currentDate: new Date().getTime(),
    maxDate: new Date().getTime(),
    accountInfo: {
      totalAmount: 0,
      totalRecycleIncome: 0,
      customerAccountDetailVoList: []
    },
  },

  onShow() {
    this.loadTransactions();
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
    } = await reqCustomerAccountInfo(deatilsTime);
    this.setData({
      accountInfo: {
        totalAmount: data.totalAmount.toFixed(2),
        totalRecycleIncome: data.totalRecycleIncome.toFixed(2),
        customerAccountDetailVoList: data.customerAccountDetailVoList
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