import {
  reqRecyclerBaseInfo
} from '../../../api/recycler/recycler'

Component({
  properties: {
    title: {
      type: String,
      value: '悦易收回收员端'
    },
    activeTab: {
      type: Number,
      value: 0
    }
  },
  data: {
    statusBarHeight: 20,
    recyclerInfo: {}
  },
  pageLifetimes: {
    show() {
      wx.getSystemSetting({
        success: (res) => {
          const systemInfo = res.systemInfo;
          this.setData({
            statusBarHeight: systemInfo.statusBarHeight
          });
        },
        fail: (err) => {
          console.error("获取系统信息失败", err);
        }
      });
      this.getRecyclerBaseInfo();
    }
  },
  methods: {
    // 获取回收员基本信息
    async getRecyclerBaseInfo() {
      const res = await reqRecyclerBaseInfo();
      this.setData({
        recyclerInfo: res.data
      });
      this.triggerEvent('getRecyclerInfo', {
        recyclerInfo: this.data.recyclerInfo
      });
    },
    // 点击头像
    onLeftTap() {
      this.triggerEvent('leftTap');
    },
    // 切换tab页
    onTabTap(e) {
      const index = e.currentTarget.dataset.index;
      const status = e.currentTarget.dataset.status;
      this.triggerEvent('tabChange', {
        index,
        status
      });
    }
  }
});