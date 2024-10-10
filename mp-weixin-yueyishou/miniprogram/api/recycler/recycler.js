import http from '../../utils/http'

// 请求地址前缀
const urlPrefix = '/recycler-api';

/**
 * 获取回收员基本信息
 */
export const reqRecyclerBaseInfo = () => {
  return http.get(urlPrefix + `/recycler/base/info`)
}

/**
 * 查询回收员是否当日是否进行了人脸识别
 */
export const reqRecyclerIsSign = () => {
  return http.get(urlPrefix + `/faceRecognition/is/sign`)
}

/**
 * 回收员进行每日人脸识别
 */
export const reqVerifyDriverFace = (data) => {
  return http.post(urlPrefix + `/faceRecognition/verifyDriverFace`, data)
}