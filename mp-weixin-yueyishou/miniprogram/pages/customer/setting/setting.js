import {
  ComponentWithStore
} from 'mobx-miniprogram-bindings'
import {
  customerStore
} from '../../../stores/customerStore'
import {
  reqCosUpload
} from '../../../api/customer/cos.js'
import {
  setStorage
} from '../../../utils/storage'
import {
  toast
} from '../../../utils/extendApi.js'
import {
  reqUpdateBaseInfo
} from '../../../api/customer/customer'

ComponentWithStore({
  // 页面的初始数据
  data: {
    isShowPopup: false // 控制更新用户昵称的弹框显示与否
  },
  storeBindings: {
    store: customerStore,
    fields: ['customerInfo'],
    actions: ['setCustomerInfo']
  },
  methods: {
    // 获取用户最新昵称
    getNickname(e) {
      // 获取用户输入的最新的昵称
      const {
        nickname
      } = e.detail.value

      this.setData({
        'customerInfo.nickname': nickname,
        isShowPopup: false
      })
    },
    // 更新用户信息
    async updateCustomerInfo() {
      const res = await reqUpdateBaseInfo(this.data.customerInfo);
      // 更新用户本地和store信息
      if (res.code == 200) {
        setStorage('customerInfo', this.data.customerInfo);
        this.setCustomerInfo(this.data.customerInfo);
        toast({
          title: '更新成功'
        })
      }
    },
    // 选择头像
    chooseAvatar(event) {
      let {
        avatarUrl
      } = event.detail
      let avatarObj = {
        detail: {
          file: {
            url: avatarUrl
          }
        }
      }
      reqCosUpload(avatarObj, 'customer/avatar').then(res => {
        this.setData({
          'customerInfo.avatarUrl': res.data.url
        })
      })

    },
    // 显示修改昵称弹框
    onUpdateNickName() {
      this.setData({
        isShowPopup: true,
        'customerInfo.nickname': this.data.customerInfo.nickname
      })
    },

    // 弹框取消按钮
    cancelForm() {
      this.setData({
        isShowPopup: false
      })
    }
  }
})