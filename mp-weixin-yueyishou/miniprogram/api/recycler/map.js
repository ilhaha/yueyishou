import http from '../../utils/http'

// 请求地址前缀
const urlPrefix = '/recycler-api';


/**
 * 回收员进行每日人脸识别
 */
export const reqUpdateRecyclerLocation = (data) => {
  return http.post(urlPrefix + `/recycler/updateRecyclerLocation`, data)
}

/**
 * 删除回收员位置信息
 */
export const reqRemoveRecyclerLocation = () => {
  return http.post(urlPrefix + `/recycler/removeRecyclerLocation`)
}