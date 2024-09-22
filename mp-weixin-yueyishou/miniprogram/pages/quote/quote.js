import {
  reqCategoryTree
} from '../../api/customer/category'

Page({
  data: {
    activeIndex: 0,
    categoryList: [],
  },
  // 获取已启动的废品品类树
  async getCategoryTree() {
    const {
      data
    } = await reqCategoryTree();
    this.setData({
      categoryList: data
    })
  },
  updateActive(event) {
    this.setData({
      activeIndex: event.currentTarget.dataset.index
    })
  },
  getCategoryData() {
    reqCategoryData().then(res => {
      this.setData({
        categoryList: res.data
      })
    })
  },
  onLoad() {
    this.getCategoryTree();
  },
})