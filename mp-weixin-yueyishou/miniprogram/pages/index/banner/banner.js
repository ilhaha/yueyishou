// pages/index/banner/banner.js

Component({

  /**
   * 组件的初始数据
   */
  data: {
    activeIndex: 0,
    // 轮播图数据
    bannerList: ["../../../assets/images/banner/banner1.webp", "../../../assets/images/banner/banner2.webp", "../../../assets/images/banner/banner3.jpeg"]
  },

  /**
   * 组件的方法列表
   */
  methods: {
    getSwiperIndex(event) {
      let {
        current
      } = event.detail;
      this.setData({
        activeIndex: current
      })
    },
  }
})