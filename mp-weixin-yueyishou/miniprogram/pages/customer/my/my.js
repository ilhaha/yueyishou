import {
  customerStore
} from '../../../stores/customerStore'
import {
  ComponentWithStore
} from 'mobx-miniprogram-bindings'
import {
  reqMyInfo
} from '../../../api/customer/customer'
import {
  toast
} from '../../../utils/extendApi';
import {
  clearStorage
} from '../../../utils/storage'

ComponentWithStore({
  storeBindings: {
    store: customerStore,
    fields: ['token', 'customerInfo'],
    actions: ['setToken']
  },
  data: {
    initCommonlyPanel: [{
        url: '/pages/customer/setting/setting',
        title: '个人设置',
        iconfont: 'icon-gerenshezhi'
      },
      {
        url: '/pages/customer/my-wallet/my-wallet',
        title: '环保金提现',
        iconfont: 'icon-tixian'
      },
      {
        url: '/pages/customer/address/list/index',
        title: '回收地址',
        iconfont: 'icon-lianxidizhi'
      },
      {
        url: '/pages/customer/service-coupon/service-coupon',
        title: '抵扣劵',
        iconfont: 'icon-shouyeicom-09'
      },
    ],
    accountBalance: 0,
    deliveryVolume: 0,
    recyclerCount: 0,
    isExitShow: false
  },
  pageLifetimes: {
    show() {
      this.init();
    }
  },
  methods: {
    // 切换退出确认框状态
    switchIsExitShow() {
      this.setData({
        isExitShow: !this.data.isExitShow
      })
    },
    // 退出
    exit() {
      this.setToken('');
      clearStorage();
      toast({
        title: '已退出',
        icon: 'success'
      })
      setTimeout(() => {
        wx.reLaunch({
          url: '/pages/index/index',
        })
      }, 1000);
    },
    async init() {
      if (!this.data.token) {
        return;
      }
      const res = await reqMyInfo();
      this.setData({
        accountBalance: res.data.accountBalance.toFixed(2),
        deliveryVolume: res.data.deliveryVolume.toFixed(2),
        recyclerCount: res.data.recyclerCount,
      })
    },
    goLogin() {
      wx.navigateTo({
        url: '/pages/login/login',
      })
    }
  }
})