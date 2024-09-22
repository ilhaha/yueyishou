Page({
  data: {
    categoryList: [], // 分类数据
    currentIndex: 0, // 当前选中的分类索引
    selectedCategory: {} // 当前选中的分类信息
  },

  onLoad(options) {
    const categoryList = JSON.parse(decodeURIComponent(options.sub));
    this.setData({
      categoryList,
      selectedCategory: categoryList[0] // 默认选中第一个分类
    });
  },

  // 选择分类的处理函数
  selectCategory(event) {
    const index = event.currentTarget.dataset.index;
    const selectedCategory = this.data.categoryList[index];

    this.setData({
      currentIndex: index, // 更新当前选中的分类索引
      selectedCategory // 更新当前选中的分类信息
    });
  }
});