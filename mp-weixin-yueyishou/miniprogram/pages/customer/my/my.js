import {
  customerStore
} from '../../../stores/customerStore'
import {
  ComponentWithStore
} from 'mobx-miniprogram-bindings'

ComponentWithStore({
  storeBindings: {
    store: customerStore,
    fields: ['token', 'customerInfo']
  },
  data: {
    initpanel: [{
        url: '/pages/customer/order/order?status=1',
        title: '待接单',
        iconfont: 'icon-daijiedan'
      },
      {
        url: '/pages/customer/order/order?status=2',
        title: '待服务',
        iconfont: 'icon-daifuwu'
      },
      {
        url: '/pages/customer/order/order?status=5',
        title: '待确认',
        iconfont: 'icon-dengdaiqueren'
      },
      {
        url: '/pages/customer/order/order?status=8',
        title: '待评价',
        iconfont: 'icon-pingjiadaiqueren'
      }
    ],
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
        url: '',
        title: '抵扣劵',
        iconfont: 'icon-shouyeicom-09'
      },
      {
        url: '',
        title: '退出',
        iconfont: 'icon-exit'
      },
    ],
  },
  methods: {
    goLogin() {
      wx.navigateTo({
        url: '/pages/login/login',
      })
    }
  }
})