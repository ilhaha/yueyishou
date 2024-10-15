import {
  reqSwitchServiceStatus
} from '../../../api/recycler/personalization'
import {
  reqRecyclerIsSign,
  reqVerifyDriverFace,
} from '../../../api/recycler/recycler'
import {
  reqCosUpload
} from '../../../api/recycler/cos'
import {
  reqUpdateRecyclerLocation,
  reqRemoveRecyclerLocation
} from '../../../api/recycler/map'
import {
  toast
} from '../../../utils/extendApi'
import {
  permissionBehavior
} from '../../../behavior/permissionBehavior'
import {
  reqRetrieveMatchingOrders,
  reqGrabOrder,
  reqOrderListByStaus
} from '../../../api/recycler/order'

Page({
  behaviors: [permissionBehavior],
  data: {
    activeTab: 0,
    recyclerInfo: {},
    menuList: [{
        text: '接单设置',
        icon: '/assets/images/common/setting.png',
        url: '/pages/recycler/order-setting/order-setting'
      },
      {
        text: '我的订单',
        icon: '/assets/images/common/my_order.png',
        url: 'goToOrders'
      },
      {
        text: '收支明细',
        icon: '/assets/images/common/income_expenditure.png',
        url: 'goToPayment'
      },
      {
        text: '我的评价',
        icon: '/assets/images/common/evaluate.png',
        url: 'goToReviews'
      },
      {
        text: '关于我们',
        icon: '/assets/images/common/about_us.png',
        url: 'goToReviews'
      }
    ],
    orderList: [],
    orderStatus: 1,
    orderListByStatus: [],
    popupShow: false,
    dialogShow: false,
    faceRecognitionList: [],
    recyclerFaceModelForm: {
      imageBase64: ''
    },
    beforeClose: null,
    currentLocation: null,
    locationIntervals: [] // 用于保存所有定时器ID
  },
  // 初始化数据
  onShow() {
    this.getRecyclerOrderListByStatus(this.data.orderStatus);
  },

  // 获取回收员订单
  async getRecyclerOrderListByStatus(status) {
    const res = await reqOrderListByStaus(status);
    this.setData({
      orderListByStatus: res.data
    })
  },

  // 抢单
  async grabOrder(event) {
    const res = await reqGrabOrder(event.currentTarget.dataset.orderid);
    toast({
      title: res.data ? '抢单成功' : '该单已被抢',
      icon: res.data ? 'success' : 'error',
      duration: 1000
    })
    this.retrieveMatchingOrders();
  },

  // 查看订单详情
  showDetails(event) {
    const orderId = event.currentTarget.dataset.orderid;
    const orderstatus = event.currentTarget.dataset.orderstatus
    wx.navigateTo({
      url: `/pages/recycler/order-details/order-details?orderId=${orderId}&orderStatus=${orderstatus}`,
    })
  },

  // 获取符合回收员的接单的订单
  async retrieveMatchingOrders() {
    const res = await reqRetrieveMatchingOrders(this.data.recyclerInfo);
    this.setData({
      orderList: res.data
    })
  },

  // 获取回收员地理位置信息
  async getCurrentLocation() {
    // 尝试先隐藏任何可能的加载或提示
    wx.hideLoading();
    wx.hideToast();

    wx.getLocation({
      type: 'gcj02', // 使用 gcj02 坐标
      success: async (res) => {
        // 成功获取用户位置
        const {
          latitude,
          longitude
        } = res;
        this.setData({
          currentLocation: {
            latitude,
            longitude
          }
        });

        // 获取成功后保存地理位置到 Redis
        await reqUpdateRecyclerLocation(this.data.currentLocation);
        // 获取符合的订单
        this.retrieveMatchingOrders();
      },
      fail: (error) => {
        // 获取失败时提示错误，不使用加载图标
        wx.showToast({
          title: '获取位置信息失败',
          icon: 'none',
          duration: 2000
        });
        console.error('获取位置信息失败：', error);
      },
      complete: () => {
        // 确保即便操作完成后也不显示加载图标
        wx.hideLoading();
        wx.hideToast();
      }
    });
  },

  // 跳转对应的菜单页
  gotoMenu(event) {
    wx.navigateTo({
      url: event.currentTarget.dataset.url
    })
  },

  // 校验人脸模型
  async verifyDriverFace() {
    this.setData({
      beforeClose: (action) =>
        new Promise((resolve) => {
          if (action === 'confirm') {
            // 确定按钮
            resolve(false)
          } else if (action === 'cancel') {
            // 取消按钮
            resolve(true)
          }
        }),
    })
    if (this.data.faceRecognitionList.length === 0) {
      wx.showToast({
        title: '请先上传人脸',
        icon: 'error',
        duration: 1000
      });
      return;
    }
    try {
      // 校验人脸
      const res = await reqVerifyDriverFace(this.data.recyclerFaceModelForm);
      if (res.data) {
        wx.showToast({
          title: '校验成功',
          icon: 'success',
          duration: 1000
        });
        // 如果校验成功，手动关闭对话框
        this.setData({
          dialogShow: false,
          'faceRecognitionList': []
        });
      } else {
        // 如果失败，则不关闭对话框，显示错误提示
        wx.showToast({
          title: '校验失败',
          icon: 'error',
          duration: 1000
        });
      }
    } catch (error) {
      wx.showToast({
        title: '验证过程中出现错误',
        icon: 'error',
        duration: 1000
      });
    }
  },

  // 上传每日人脸模型
  async uploadFaceModel(event) {
    const res = await reqCosUpload(event, 'recycler');
    // 上传失败
    if (res.code != 200) {
      wx.showToast({
        title: res.message,
        icon: 'error',
        duration: 1000
      });
      return;
    }

    const fileUrl = res.data.url; // 获取文件URL

    const newFileList = this.data.faceRecognitionList.concat({
      url: fileUrl
    });
    this.setData({
      faceRecognitionList: newFileList
    });

    try {
      // 通过 wx.downloadFile 下载文件
      const downloadRes = await this.downloadFile(fileUrl);

      // 通过 wx.getFileSystemManager 将文件内容读取为 base64
      const base64Data = await this.convertToBase64(downloadRes.tempFilePath);
      this.setData({
        'recyclerFaceModelForm.imageBase64': base64Data
      })

      // 这里可以根据需求处理 base64 数据，比如存储或者上传
    } catch (error) {
      console.error("Error converting to base64:", error);
    }
  },

  // 下载文件的方法
  downloadFile(fileUrl) {
    return new Promise((resolve, reject) => {
      wx.downloadFile({
        url: fileUrl,
        success(res) {
          if (res.statusCode === 200) {
            resolve(res); // 成功返回文件路径
          } else {
            reject('文件下载失败');
          }
        },
        fail(err) {
          reject(err);
        }
      });
    });
  },

  // 将文件转换为 Base64 编码的方法
  convertToBase64(filePath) {
    return new Promise((resolve, reject) => {
      wx.getFileSystemManager().readFile({
        filePath: filePath,
        encoding: 'base64', // 指定编码为 base64
        success(res) {
          // 给 base64 添加适当的前缀
          const base64Data = 'data:image/png;base64,' + res.data;
          resolve(base64Data); // 返回 base64 编码
        },
        fail(err) {
          reject(err);
        }
      });
    });
  },

  // 删除每日人脸模型
  deleteFaceRecognition(event) {
    const {
      index
    } = event.detail; // 获取被删除的图片索引
    const {
      faceRecognitionList
    } = this.data; // 获取当前图片列表

    // 删除索引对应的图片
    faceRecognitionList.splice(index, 1);

    // 更新数据
    this.setData({
      faceRecognitionList
    });
  },

  // 切换回收员服务状态
  async switchServiceStatus(event) {
    if (event.detail) {
      const {
        data
      } = await reqRecyclerIsSign();
      // 没进行每日人脸识别
      if (!data) {
        this.switchDialog();
        return;
      }
    }

    const hasPermission = await this.userPermission('scope.userLocation');
    if (!hasPermission) {
      toast({
        title: "拒绝授权",
        icon: 'error'
      });
      return;
    }

    if (event.detail) {
      // 开始每隔 30 秒获取一次定位并获取符合接单要求的新订单
      this.startLocationUpdates();
    } else {
      // 停止获取定位
      this.stopLocationUpdates();
    }

    await reqSwitchServiceStatus(event.detail ? 1 : 0);
    this.setData({
      'recyclerInfo.serviceStatus': event.detail ? 1 : 0
    });
  },

  // 开始接单
  async takeOrders() {
    const {
      data
    } = await reqRecyclerIsSign();
    // 没进行每日人脸识别
    if (!data) {
      this.switchDialog()
      return;
    }
    // 判断是否有定位授权
    const hasPermission = await this.userPermission('scope.userLocation');
    if (!hasPermission) {
      toast({
        title: "拒绝授权",
        icon: 'error'
      })
      return
    }
    // 上传定位
    this.startLocationUpdates();
    const res = await reqSwitchServiceStatus(1)
    if (res.data) {
      toast({
        title: '开始服务',
        icon: 'success'
      })
      this.setData({
        'recyclerInfo.serviceStatus': 1
      })
    }
  },

  // 开始获取定位，每 30 秒获取一次
  startLocationUpdates() {
    // 先清除所有现有的定时器，避免重复
    this.stopLocationUpdates();

    this.getCurrentLocation(); // 立即获取一次
    const locationInterval = setInterval(() => {
      this.getCurrentLocation();
    }, 30000); // 每隔30秒获取一次

    // 存储定时器ID
    this.data.locationIntervals.push(locationInterval);
  },

  // 停止获取定位
  async stopLocationUpdates() {
    // 遍历数组，清除每一个定时器
    if (this.data.locationIntervals.length > 0) {
      this.data.locationIntervals.forEach(interval => {
        clearInterval(interval);
      });
      // 清空定时器数组
      this.setData({
        locationIntervals: []
      });
    }
    // 清空回收员的位置信息
    await reqRemoveRecyclerLocation();
  },

  // 获取回收员信息
  getRecyclerInfo(e) {
    this.setData({
      recyclerInfo: e.detail.recyclerInfo
    })
    if (this.data.recyclerInfo.serviceStatus == 1) {
      this.startLocationUpdates();
    }
  },

  // 切换tab页
  onTabChange(e) {
    const status = e.detail.status || 1;
    const index = e.detail.index;
    index == 0 ? (this.data.recyclerInfo.serviceStatus == 1 ? this.retrieveMatchingOrders() : '') : this.getRecyclerOrderListByStatus(status);
    this.setData({
      activeTab: index,
      orderStatus: status
    });
  },

  switchDialog() {
    this.setData({
      dialogShow: !this.data.dialogShow
    })
    if (!this.data.dialogShow) {
      this.setData({
        'recyclerFaceModelForm.imageBase64': '',
        'faceRecognitionList': []
      })
    }
  },

  // 点击头像按钮
  onLeftTap() {
    this.setData({
      popupShow: !this.data.popupShow
    })
  },
});