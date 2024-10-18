import {
  reqCategoryTree
} from '../../../api/recycler/category';
import {
  reqCosUpload
} from '../../../api/recycler/cos'
import {
  reqCalculateOrderFee,
  reqUpdateOrder
} from '../../../api/recycler/order'

Page({
  data: {
    orderInfo: {}, // 订单信息
    categoryTree: [], // 分类树数据
    chooseCategoryShow: false, // 是否显示弹窗
    cascaderCategoryValue: [], // 保存一级和二级分类ID
    chooseCategoryValue: '', // 展示选中的分类（名称）
    chooseCategoryFieldNames: {
      text: 'categoryName', // 字段名映射：分类名称
      value: 'id', // 字段名映射：分类 ID
      children: 'children', // 字段名映射：子分类
    },
    actualPhotos: [],
    updateOrderForm: {
      id: '',
      parentCategoryId: '', // 一级分类ID
      parentCategoryName: '', // 一级分类名称
      sonCategoryId: '', // 二级分类ID
      sonCategoryName: '', // 二级分类名称
      unitPrice: '', // 单价
      recycleWeigh: 0,
      actualPhotos: "",
      estimatedTotalAmount: 0,
      expectCustomerPlatformAmount: 0,
      expectRecyclerPlatformAmount: 0
    },
    dialogShow: false
  },

  // 页面加载时获取订单信息和分类数据
  async onLoad(option) {
    const orderInfo = JSON.parse(decodeURIComponent(option.orderInfo));
    let actualPhotos = []; // 正确的变量名
    for (let i = 0; i < orderInfo.actualPhotoList.length; i++) {
      actualPhotos.push({
        url: orderInfo.actualPhotoList[i] // 使用 i 遍历所有图片
      });
    }

    this.setData({
      orderInfo: orderInfo,
      chooseCategoryValue: `${orderInfo.parentCategoryName} / ${orderInfo.sonCategoryName}`,
      cascaderCategoryValue: [orderInfo.parentCategoryId, orderInfo.sonCategoryId],
      updateOrderForm: {
        id: orderInfo.id,
        parentCategoryId: orderInfo.parentCategoryId,
        parentCategoryName: orderInfo.parentCategoryName,
        sonCategoryId: orderInfo.sonCategoryId,
        sonCategoryName: orderInfo.sonCategoryName,
        unitPrice: orderInfo.unitPrice,
        recycleWeigh: orderInfo.recycleWeigh
      },
      actualPhotos: actualPhotos
    });
    await this.getCategoryTree();
  },

  // 预估订单金额
  async estimatedAmount() {
    const {
      sonCategoryName,
      recycleWeigh,
    } = this.data.updateOrderForm;

    // 校验表单
    if (!sonCategoryName) {
      return wx.showToast({
        title: '请选择回收品类',
        icon: 'none'
      });
    }
    if (!recycleWeigh) {
      return wx.showToast({
        title: '请输入回收重量',
        icon: 'none'
      });
    }
    if (this.data.actualPhotos.length === 0) {
      return wx.showToast({
        title: '请上传实物照片',
        icon: 'none'
      });
    }

    const res = await reqCalculateOrderFee({
      unitPrice: this.data.updateOrderForm.unitPrice,
      recycleWeigh: this.data.updateOrderForm.recycleWeigh
    });
    this.setData({
      updateOrderForm: {
        ...this.data.updateOrderForm,
        estimatedTotalAmount: res.data.estimatedTotalAmount,
        expectCustomerPlatformAmount: res.data.expectCustomerPlatformAmount,
        expectRecyclerPlatformAmount: res.data.expectRecyclerPlatformAmount
      }
    })
    this.switchDialogStatus();
  },

  // 切换Dialog框状态
  switchDialogStatus() {
    this.setData({
      dialogShow: !this.data.dialogShow
    })
  },

  // 提交表单
  async onSubmit() {
    this.setData({
      updateOrderForm: {
        ...this.data.updateOrderForm,
        actualPhotos: this.data.actualPhotos.map(photo => photo.url).join(',')
      }
    })
    const res = await reqUpdateOrder(this.data.updateOrderForm);
    if (res.data) {
      wx.showToast({
        title: '修改成功',
        icon: 'success'
      });
      let orderId = this.data.updateOrderForm.id;
      wx.navigateBack({
        delta: 1, // 返回到上一个页面
        success: function () {
          const pages = getCurrentPages(); // 获取当前页面栈
          const prevPage = pages[pages.length - 1]; // 上一个页面
          if (prevPage) {
            // 可以手动调用 prevPage 的方法，或重新设置数据
            prevPage.getOrderInfo(orderId);
          }
        }
      });
    }
  },

  // 更新实物图片
  async reqCosUpload(event) {
    const res = await reqCosUpload(event, 'order');
    // 构造新的文件对象
    let file = {
      url: res.data.url
    }
    // 使用扩展运算符生成新的 fileList 数组
    this.setData({
      actualPhotos: [...this.data.actualPhotos, file]
    })
  },

  // 处理删除图片
  onDelete(event) {
    const index = event.detail.index; // 获取被删除的图片索引
    const newFileList = [...this.data.actualPhotos];
    newFileList.splice(index, 1); // 删除对应索引的图片

    this.setData({
      actualPhotos: newFileList
    });
  },

  // 更新回收重量
  changeWeight(event) {
    this.setData({
      'updateOrderForm.recycleWeigh': event.detail
    })

  },

  // 切换选择弹窗的显示状态
  switchChooseCategoryShow() {
    this.setData({
      chooseCategoryShow: !this.data.chooseCategoryShow,
    });
  },

  // 获取分类树数据
  async getCategoryTree() {
    const res = await reqCategoryTree();
    this.setData({
      categoryTree: res.data,
    });
  },

  // 处理分类选择变化
  onCategoryChange(event) {
    const {
      selectedOptions
    } = event.detail; // 获取选中的分类值和选项

    if (selectedOptions.length === 2) { // 如果选择了两层
      const parentCategory = selectedOptions[0]; // 一级分类
      const sonCategory = selectedOptions[1]; // 二级分类
      const selectedCategory = `${parentCategory.categoryName} / ${sonCategory.categoryName}`; // 拼接名称

      this.setData({
        cascaderCategoryValue: [parentCategory.id, sonCategory.id], // 保存一级和二级分类ID
        chooseCategoryValue: selectedCategory, // 保存展示用名称
        chooseCategoryShow: false, // 关闭弹窗
        updateOrderForm: {
          ...this.data.updateOrderForm,
          parentCategoryId: parentCategory.id,
          parentCategoryName: parentCategory.categoryName,
          sonCategoryId: sonCategory.id,
          sonCategoryName: sonCategory.categoryName,
          unitPrice: sonCategory.unitPrice,
        }
      });
    }
  },
});