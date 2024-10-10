import {
  reqCategoryParent
} from '../../../api/recycler/category.js'
import {
  reqOrderSetting,
  reqUpdateOrderSetting
} from '../../../api/recycler/personalization'
import {
  toast
} from '../../../utils/extendApi'

Page({
  data: {
    categoryList: [], // 保存回收品类
    orderSetting: {}, // 保存接单设置
    currentIndexList: [] // 保存高亮显示的品类索引
  },

  onShow() {
    // 先获取回收品类和接单设置
    this.getCategoryParentList();
  },

  // 修改里程
  setAcceptDistance(event) {
    this.setData({
      'orderSetting.acceptDistance': event.detail
    })
  },

  // 修改接单设置
  async saveSetting() {
    const selectedCategories = this.data.categoryList
      .filter(item => item.isActive) // 过滤出选中的品类
      .map(item => item.id); // 获取选中的品类的id数组

    if (selectedCategories.length === 0) {
      // 如果没有选择任何品类，提示用户必须选择一个
      wx.showToast({
        title: '请选择废品品类',
        icon: 'error',
        duration: 1000
      });
      return;
    }

    // 修改接单设置
    const res = await reqUpdateOrderSetting({
      recyclingType: selectedCategories.join(','),
      acceptDistance: this.data.orderSetting.acceptDistance
    })
    if (res.data) {
      toast({
        title: '修改成功',
        icon: 'success'
      })
      setTimeout(() => {
        wx.navigateBack();
      }, 1000);
    }
  },
  // 获取回收员当前的接单设置
  async getRecyclerOrderSetting() {
    const res = await reqOrderSetting();
    const recyclingTypeArray = res.data.recyclingType ? res.data.recyclingType.split(',') : []; // 将recyclingType字符串分割为数组

    this.setData({
      orderSetting: res.data
    });

    // 调用方法设置高亮品类
    this.updateCategoryHighlight(recyclingTypeArray);
  },

  // 获取废品父品类
  async getCategoryParentList() {
    const res = await reqCategoryParent();
    this.setData({
      categoryList: res.data
    }, () => {
      // 确保获取完 `categoryList` 后再获取接单设置
      this.getRecyclerOrderSetting();
    });
  },

  // 更新categoryList中的高亮状态
  updateCategoryHighlight(recyclingTypeArray) {
    const {
      categoryList
    } = this.data;
    const updatedCategoryList = categoryList.map((item) => {
      return {
        ...item,
        isActive: recyclingTypeArray.includes(String(item.id)) // 添加isActive属性
      };
    });

    this.setData({
      categoryList: updatedCategoryList // 更新categoryList，包含isActive状态
    });
  },

  // 选择回收品类
  selectCategory(event) {
    const {
      id,
      index
    } = event.currentTarget.dataset;
    const {
      categoryList
    } = this.data;

    // 切换选中状态
    const updatedCategoryList = categoryList.map((item, idx) => {
      if (idx === index) {
        return {
          ...item,
          isActive: !item.isActive
        };
      }
      return item;
    });

    this.setData({
      categoryList: updatedCategoryList // 更新品类高亮状态
    });
  },
});