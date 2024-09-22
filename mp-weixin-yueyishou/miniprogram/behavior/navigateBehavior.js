import {
  customerStore
} from '../stores/customerStore'
import {
  toast
} from '../utils/extendApi'

export const navigateBehavior = Behavior({

  methods: {
    // 需要校验的登录状态的界面跳转
    checkLoginAndNavigate(event) {
      let isLogin = customerStore.token != null && customerStore.token != '';
      const url = isLogin ? event.currentTarget.dataset.url : '/pages/login/login';
      wx.navigateTo({
        url
      });
      if (!isLogin) {
        toast({
          title: '您未登录，请您先登录',
          duration: 1000
        })
      }
    },
    // 不需要校验的登录状态的界面跳转
    navigateToPage(event) {
      const url = event.currentTarget.dataset.url;
      wx.navigateTo({
        url
      });
    }
  }
})