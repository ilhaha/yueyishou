import {
  reqSwitchServiceStatus
} from '../../../api/recycler/personalization'
import {
  reqRecyclerIsSign,
  reqVerifyDriverFace
} from '../../../api/recycler/recycler'
import {
  reqCosUpload
} from '../../../api/recycler/cos'

import {
  toast
} from '../../../utils/extendApi'
Page({
  data: {
    activeTab: 0,
    recyclerInfo: {},
    orderList: [],
    popupShow: false,
    dialogShow: false,
    faceRecognitionList: [],
    recyclerFaceModelForm: {
      imageBase64: ''
    },
    beforeClose: null,
  },
  // 初始化数据
  onLoad() {},

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
          'recyclerInfo.serviceStatus': 1,
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
        this.switchDialog()
        return;
      }
    }
    await reqSwitchServiceStatus(event.detail ? 1 : 0);
    this.setData({
      'recyclerInfo.serviceStatus': event.detail ? 1 : 0
    })
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

  // 获取回收员信息
  getRecyclerInfo(e) {
    this.setData({
      recyclerInfo: e.detail.recyclerInfo
    })
  },

  // 切换tab页
  onTabChange(e) {
    const status = e.detail.status || 1;
    const index = e.detail.index;
    console.log(status);
    this.setData({
      activeTab: index
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