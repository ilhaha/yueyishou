import {
  reqAddressDefault,
  reqAddressList
} from '../../api/customer/address'

import {
  reqCosUpload
} from '../../api/customer/cos'

import {
  reqCalculateOrderFee,
  reqPlaceOrder
} from '../../api/customer/order'

import {
  toast
} from '../../utils/extendApi.js'

Page({
  data: {
    categoryList: [], // 分类数据
    currentIndex: 0, // 当前选中的分类索引
    selectedCategory: {}, // 当前选中的分类信息
    address: {}, // 选择的地址信息
    show: false,
    dialogShow: false,
    addressList: [],
    addAddressShow: false,
    weightList: [{
        id: 1,
        desc: '约10-100公斤',
        value: 100
      },
      {
        id: 2,
        desc: '约200-500公斤',
        value: 500
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
    multiArray: [
      ['今天', '明天', '后天'],
      []
    ],
    multiIndex: [0, 0],
    normalHours: [],
    todayHours: [],
    formattedDate: '', // 用于显示选择后的时间
    orderForm: {
      customerLocation: '',
      customerPointLongitude: '',
      customerPointLatitude: '',
      parentCategoryId: '',
      parentCategoryName: '',
      sonCategoryId: '',
      sonCategoryName: '',
      unitPrice: '',
      appointmentTime: '',
      actualPhotos: '',
      recycleWeigh: '',
      remark: '',
      expectRecyclerPlatformAmount: '',
      expectCustomerPlatformAmount: '',
      orderContactPerson: '',
      orderContactPhone: '',
    }
  },
  // 初始化数据
  onLoad(options) {
    const categoryList = JSON.parse(decodeURIComponent(options.sub));
    this.setData({
      categoryList,
      selectedCategory: categoryList[0], // 默认选中第一个分类
      selectedWeight: this.data.weightList[0], // 默认选中第一个重量
      'orderForm.parentCategoryId': options.parentCategoryId,
      'orderForm.parentCategoryName': options.parentCategoryName
    });
    this.getDefaultAddress();
  },
  // 确认下单
  async placeOrder(event) {
    const res = await reqPlaceOrder(this.data.orderForm);
    if (res.data) {
      toast({
        title: '预约成功',
        icon: 'success',
        duration: 1000 // 设置显示时间为1.5秒
      });

      // 等待toast显示完成后再跳转页面
      setTimeout(() => {
        wx.switchTab({
          url: '/pages/customer/order/order'
        });
      }, 1000); // 延迟1.5秒跳转，与toast显示时间一致

    }
  },

  // 预估回收金额
  async estimatedAmount() {
    // 校验参数
    if (this.checkParams()) {
      return;
    }
    const res = await reqCalculateOrderFee({
      unitPrice: this.data.selectedCategory.unitPrice,
      recycleWeigh: this.data.selectedWeight.value
    });
    const data = res.data;
    this.setData({
      'orderForm.estimatedTotalAmount': data.estimatedTotalAmount,
      'orderForm.expectRecyclerPlatformAmount': data.expectRecyclerPlatformAmount,
      'orderForm.expectCustomerPlatformAmount': data.expectCustomerPlatformAmount,
    })
    this.checkOrder();
  },
  // 整理订单信息
  checkOrder() {
    // 整理参数
    const {
      address,
      selectedCategory,
      selectedWeight,
      formattedDate,
      fileList,
      orderForm
    } = this.data;
    // 填充 orderForm 参数
    this.setData({
      'orderForm.customerLocation': address.fullAddress, // 设置回收地址
      'orderForm.customerPointLongitude': address.longitude, // 设置地址经度
      'orderForm.customerPointLatitude': address.latitude, // 设置地址纬度
      'orderForm.unitPrice': selectedCategory.unitPrice, // 单价
      'orderForm.appointmentTime': formattedDate, // 设置预约时间
      'orderForm.actualPhotos': fileList.map(file => file.url).join(','), // 将图片URL拼接为字符串
      'orderForm.recycleWeigh': selectedWeight.value, // 设置重量值
      'orderForm.orderContactPerson': address.name,
      'orderForm.orderContactPhone': address.phone,
      'orderForm.sonCategoryId': selectedCategory.id,
      'orderForm.sonCategoryName': selectedCategory.categoryName,
      dialogShow: true
    });
  },
  // 校验参数
  checkParams() {
    const {
      address,
      selectedCategory,
      formattedDate,
      fileList,
      selectedWeight
    } = this.data;

    if (!address.fullAddress) {
      wx.showToast({
        title: '请选择回收地址',
        icon: 'none'
      });
      return true;
    }

    if (!selectedCategory.id) {
      wx.showToast({
        title: '请选择回收品类',
        icon: 'none'
      });
      return true;
    }

    if (!selectedWeight.value) {
      wx.showToast({
        title: '请选择回收重量',
        icon: 'none'
      });
      return true;
    }

    if (!formattedDate) {
      wx.showToast({
        title: '请选择上门时间',
        icon: 'none'
      });
      return true;
    }

    if (fileList.length === 0) {
      wx.showToast({
        title: '请上传至少一张回收物的照片',
        icon: 'none'
      });
      return true;
    }

    return false;
  },

  // 获取备注信息
  getRemark(event) {
    this.setData({
      'orderForm.remark': event.detail
    });
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
  // 关闭确认订单弹出
  onClose() {
    this.setData({
      dialogShow: false
    });
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
  },

  onShow() {
    this.initHour();
  },

  // 初始化小时数组
  initHour() {
    const hour = new Date().getHours();
    const minute = new Date().getMinutes();

    // 如果分钟不是整点，+1小时开始，否则直接从当前小时开始
    const todayStartHour = minute > 0 ? (hour >= 8 ? hour + 1 : 8) : (hour >= 8 ? hour : 8);

    const todayHours = this.generateTodayHoursArray(todayStartHour); // 从下一个小时开始生成“今天”的数组
    const normalHours = this.generateHoursArray(); // 生成“明天”和“后天”的默认小时数组

    const multiArray = this.data.multiArray;

    multiArray[1] = todayHours; // 设置“今天”的小时数组
    this.setData({
      todayHours: todayHours,
      normalHours: normalHours,
      multiArray: todayHours.length > 0 ? multiArray : [
        ['明天', '后天'], normalHours
      ],
    });
  },

  // 处理picker改变
  bindMultiPickerChange: function (e) {
    const {
      multiArray,
      multiIndex
    } = this.data;
    const selectedDay = multiArray[0][multiIndex[0]]; // 选择的天
    const selectedTime = multiArray[1][multiIndex[1]]; // 选择的时间段

    const endTime = selectedTime.split('-')[1]; // 获取结束时间，如 "10:00"

    const formattedDate = this.formatSelectedDate(selectedDay, endTime.trim()); // 将结束时间传递
    this.setData({
      multiIndex: e.detail.value,
      formattedDate: formattedDate
    });
  },

  // 处理列改变
  bindMultiPickerColumnChange: function (e) {
    const column = e.detail.column;
    const value = e.detail.value;
    const data = {
      multiArray: this.data.multiArray,
      multiIndex: this.data.multiIndex
    };
    data.multiIndex[column] = value;

    switch (column) {
      case 0: // 第一列的变化
        switch (value) {
          case 0:
            data.multiArray[1] = this.data.todayHours.length > 0 && data.multiArray[0].length > 2 ? this.data.todayHours : this.data.normalHours; // 切换到今天时间数组
            break;
          case 1:
          case 2:
            data.multiArray[1] = this.data.normalHours; // 明天、后天选择全天时间
            break;
        }
        break;
    }
    this.setData(data);
  },

  // 生成“今天”的时间数组，从当前时间的下一个小时开始
  generateTodayHoursArray(start) {
    const arrayHours = [];
    for (let i = start; i < 21; i++) { // 限制时间段到 06:00-20:00
      const hourStr = `${i < 10 ? '0' : ''}${i}:00-${i + 1 < 10 ? '0' : ''}${i + 1}:00`;
      arrayHours.push(hourStr);
    }
    return arrayHours;
  },

  // 生成“明天”和“后天”的时间数组，默认从 06:00 开始
  generateHoursArray(start = 8) {
    const arrayHours = [];
    for (let i = start; i < 21; i++) {
      const hourStr = `${i < 10 ? '0' : ''}${i}:00-${i + 1 < 10 ? '0' : ''}${i + 1}:00`;
      arrayHours.push(hourStr);
    }
    return arrayHours;
  },
  // 格式化选择的时间为 "yyyy-MM-dd HH:mm"
  formatSelectedDate(selectedDay, selectedTime) {
    const today = new Date();
    let selectedDate = new Date();

    if (selectedDay === '今天') {
      selectedDate = today;
    } else if (selectedDay === '明天') {
      selectedDate.setDate(today.getDate() + 1);
    } else if (selectedDay === '后天') {
      selectedDate.setDate(today.getDate() + 2);
    }

    const [hour, minute] = selectedTime.split(':');
    selectedDate.setHours(parseInt(hour), parseInt(minute));

    const year = selectedDate.getFullYear();
    const month = (selectedDate.getMonth() + 1).toString().padStart(2, '0');
    const day = selectedDate.getDate().toString().padStart(2, '0');
    const formattedHour = selectedDate.getHours().toString().padStart(2, '0');
    const formattedMinute = selectedDate.getMinutes().toString().padStart(2, '0');

    return `${year}-${month}-${day} ${formattedHour}:${formattedMinute}`;
  }
});