import http from '../../utils/http'

// 请求地址前缀
const urlPrefix = '/recycler-api';

/**
 * 切换回收员服务状态
 */
export const reqSwitchServiceStatus = (serviceStatus) => {
  return http.post(urlPrefix + `/personalization/switch/service/${serviceStatus}`)
}

/**
 * 获取回收员当前的接单设置
 */
export const reqOrderSetting = () => {
  return http.get(urlPrefix + `/personalization/order/setting`)
}

/**
 * 修改回收员接单设置
 */
export const reqUpdateOrderSetting = (data) => {
  return http.post(urlPrefix + `/personalization/update/order/setting`, data)
}