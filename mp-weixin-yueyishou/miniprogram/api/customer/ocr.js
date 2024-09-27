import {
  customerStore
} from '../../stores/customerStore'
/**
 * 身份证识别
 */
export const idCardOcr = (event) => {
  const {
    file
  } = event.detail;
  // 返回一个 Promise 对象
  return new Promise((resolve, reject) => {
    wx.uploadFile({
      url: 'http://localhost/customer-api/ocr/idCardOcr',
      filePath: file.url,
      name: 'file',
      header: {
        'token': customerStore.token
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