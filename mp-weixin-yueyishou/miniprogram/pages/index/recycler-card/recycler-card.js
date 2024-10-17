import {
  navigateBehavior
} from '../../../behavior/navigateBehavior';
import {
  customerStore
} from '../../../stores/customerStore'
import {
  ComponentWithStore
} from 'mobx-miniprogram-bindings'
import {
  toast
} from '../../../utils/extendApi';
import {
  reqReplenishInfo
} from '../../../api/customer/customer'


ComponentWithStore({

  behaviors: [navigateBehavior],
  storeBindings: {
    store: customerStore,
    fields: ['token']
  },
  /**
   * 组件的初始数据
   */
  data: {
    certificationShow: false,
  },

  /**
   * 组件的方法列表
   */
  methods: {
    // 注册成为回收员
    async registerRecycler() {
      if (this.data.token) {
        // 以防如果用户还未退出登录就已经审核成功出现信息不全问题
        const res = await reqReplenishInfo();
        if (res.code != 200) {
          toast({
            title: res.message,
            icon: 'error',
            duration: 1000
          })
          return;
        } else {
          this.switchCertificationShow();
        }

      } else {
        toast({
          title: "您未登录，请您先登录"
        })
        wx.navigateTo({
          url: '/pages/login/login'
        })
      }
    },
    // 审核通过，跳转回收员端
    gotoRecyclerIndex(event) {
      const data = event.detail;
      this.setData({
        certificationShow: false
      })
      if (data == 2) {
        wx.showTabBar({
          animation: true // 可选：带动画效果
        });
        wx.navigateTo({
          url: '/pages/recycler/index/index'
        })

      }

    },
    // 打开或隐藏填写认证信息窗口
    switchCertificationShow() {
      const newStatus = !this.data.certificationShow;

      // 更新 certificationShow 状态
      this.setData({
        certificationShow: newStatus
      });

      // 判断是否需要隐藏或显示 tabBar
      if (newStatus) {
        // 如果 newStatus 为 true，隐藏 tabBar
        wx.hideTabBar({
          animation: true // 可选：带动画效果
        });
      } else {
        // 如果 newStatus 为 false，显示 tabBar
        wx.showTabBar({
          animation: true // 可选：带动画效果
        });
      }
    }

  }
})