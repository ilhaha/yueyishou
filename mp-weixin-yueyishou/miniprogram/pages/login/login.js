import {
  toast
} from '../../utils/extendApi'
import {
  setStorage
} from '../../utils/storage'
import {
  reqLogin,
  reqCustomerLoginInfo
} from '../../api/customer/customer'
import {
  ComponentWithStore
} from 'mobx-miniprogram-bindings'
import {
  customerStore
} from '../../stores/customerStore'

ComponentWithStore({
  data: {},
  storeBindings: {
    store: customerStore,
    fields: ['token', 'customerInfo'],
    actions: ['setToken', 'setCustomerInfo']
  },
  methods: {
    login() {
      wx.login({
        success: ({
          code
        }) => {
          if (code) {
            reqLogin(code).then(res => {
              setStorage('token', res.data)
              this.setToken(res.data)
              // 获取顾客信息
              this.getCustomerInfo();
              // 返回上一个页面
              wx.navigateBack()
            })
          } else {
            toast({
              title: '授权失败，请稍后再试~~~'
            })
          }
        },
      })
    },
    getCustomerInfo() {
      reqCustomerLoginInfo().then(res => {
        setStorage('customerInfo', res.data)
        this.setCustomerInfo(res.data)
      })
    },
  }
})