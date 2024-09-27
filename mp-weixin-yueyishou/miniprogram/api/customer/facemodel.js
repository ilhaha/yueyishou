import {
  customerStore
} from '../../stores/customerStore'

import http from '../../utils/http'

// 请求地址前缀
const urlPrefix = '/customer-api';

/**
 * 创建人脸模型
 */
export const reqCreateFaceModel = (event, params) => {
  const {
    file
  } = event.detail;
  // 返回一个 Promise 对象
  return new Promise((resolve, reject) => {
    wx.uploadFile({
      url: 'http://localhost/customer-api/faceModel/create',
      filePath: file.url,
      name: 'file',
      header: {
        'token': customerStore.token
      },
      formData: {
        faceModelForm: JSON.stringify(params)
      },
      success(res) {
        // 上传成功，解析响应结果并调用 resolve
        try {
          const data = JSON.parse(res.data);
          resolve(data); // 传递解析后的数据
        } catch (err) {
          reject(err); // 解析失败时调用 reject
        }
      },
      fail(err) {
        reject(err); // 上传失败时调用 reject
      }
    });
  });
}

/**
 * 删除人脸模型
 */
export const reqDeleteFaceModel = () => {
  return http.post(urlPrefix + `/faceModel/remove`)
}