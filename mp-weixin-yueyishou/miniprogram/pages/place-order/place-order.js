import {
  reqAddressDefault,
  reqAddressList
} from '../../api/customer/address'

import {
  reqCosUpload
} from '../../api/customer/cos'

Page({
  data: {
    categoryList: [], // 分类数据
    currentIndex: 0, // 当前选中的分类索引
    selectedCategory: {}, // 当前选中的分类信息
    address: {}, // 选择的地址信息
    show: false,
    addressList: [],
    addAddressShow: false,
    weightList: [{
        id: 1,
        desc: '约10-100公斤',
        value: 50
      },
      {
        id: 2,
        desc: '约200-500公斤',
        value: 350
      },
      {
        id: 3,
        desc: '1000公斤以上',
        value: 1000
      },
    ],
    weightCurrentIndex: 0, // 当前选中的分类索引
    selectedWeight: {}, // 当前选中的分类信息
    fileList: [],
    orderForm: {
      categoryId: '',
      address: {},
      weightValue: '',
      fileList: [],
      remark: ''
    }
  },

  onLoad(options) {
    const categoryList = JSON.parse(decodeURIComponent(options.sub));
    this.setData({
      categoryList,
      selectedCategory: categoryList[0], // 默认选中第一个分类
      selectedWeight: this.data.weightList[0] // 默认选中第一个重量
    });
    this.getDefaultAddress();
  },
  // 跳转条款页面
  goTerms() {
    wx.navigateTo({
      url: '../place-order/terms/terms',
    })
  },
  // 获取备注信息
  getRemark(event) {
    this.setData({
      'orderForm.remark': event.detail
    })
  },
  // 上传图片
  async reqCosUpload(event) {
    const res = await reqCosUpload(event, 'order');
    // 构造新的文件对象
    let file = {
      url: res.data.url
    }
    // 使用扩展运算符生成新的 fileList 数组
    this.setData({
      fileList: [...this.data.fileList, file] // 扩展原数组并添加新文件
    })

  },
  // 处理删除图片
  onDelete(event) {
    const index = event.detail.index; // 获取被删除的图片索引
    const newFileList = [...this.data.fileList]; // 复制当前的 fileList
    newFileList.splice(index, 1); // 删除对应索引的图片

    this.setData({
      fileList: newFileList // 更新 fileList
    });
  },
  // 关闭新增地址区域
  closeAddArea() {
    const addAddressComponent = this.selectComponent('#add-address'); // 替换为正确的选择器
    if (addAddressComponent) {
      addAddressComponent.clearData();
    }
    this.setData({
      addAddressShow: false,
      show: true
    });
  },
  // 新增地址成功之后
  onAddAddressSuccess(event) {
    // 更新父页面的状态或做其他操作
    this.getAddressList();
    this.setData({
      addAddressShow: event.detail,
      show: true
    });
  },
  // 打开新增回收地址弹窗
  openAddAddressArea() {
    this.setData({
      addAddressShow: true,
      show: false
    })
  },
  // 关闭回收地址列表
  hideSheet() {
    this.setData({
      show: false
    })
  },
  // 获取当前登录的顾客地址信息
  async getAddressList() {
    const {
      data
    } = await reqAddressList();
    const {
      address
    } = this.data;
    let addressFound = false; // 标记是否找到匹配的地址

    // 将地址列表中的第一个地址默认设置为选中
    const addressList = data.map((item, index) => {
      // 检查当前地址是否和已有的 address 一致
      const isSelected = address && item.id === address.id;
      if (isSelected) {
        addressFound = true; // 找到匹配的地址
      }
      return {
        ...item,
        initial: item.name.substr(0, 1), // 提取姓名的首字母
        selected: isSelected // 设置是否选中
      };
    });

    // 如果没有匹配的地址，则默认选择第一个地址
    if (!addressFound && addressList.length > 0) {
      addressList[0].selected = true;
    }

    this.setData({
      addressList
    });
  },

  // 打开选择地址弹窗
  chooseAddress() {
    this.getAddressList(); // 获取地址列表并设置默认选中状态
    this.setData({
      show: true
    });
  },

  // 选择回收地址
  selectAddress(e) {
    const {
      index
    } = e.currentTarget.dataset;
    const addressList = this.data.addressList.map((item, idx) => ({
      ...item,
      selected: idx === index // 选中当前点击的地址
    }));
    this.setData({
      addressList
    });
  },

  // 确认回收地址
  confirm() {
    const selectedAddress = this.data.addressList.find(item => item.selected);
    this.setData({
      address: selectedAddress, // 设置选择的地址为当前地址
      show: false // 关闭弹窗
    });
  },

  // 获取顾客的默认地址
  async getDefaultAddress() {
    const {
      data
    } = await reqAddressDefault();
    this.setData({
      address: data
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
  },
  // 选择重量的处理函数
  selectWeight(event) {
    const index = event.currentTarget.dataset.index;
    const selectedWeight = this.data.weightList[index];

    this.setData({
      weightCurrentIndex: index, // 更新当前选中的分类索引
      selectedWeight // 更新当前选中的分类信息
    });
  }
});