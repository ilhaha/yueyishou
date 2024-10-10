import http from '../../utils/http'

// 请求地址前缀
const urlPrefix = '/recycler-api';

/**
 * 切换回收员服务状态
 */
export const reqSwitchServiceStatus = (serviceStatus) => {
  return http.post(urlPrefix + `/personalization/switch/service/${serviceStatus}`)
}