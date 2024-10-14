import http from '../../utils/http'

// 请求地址前缀
const urlPrefix = '/recycler-api';

/**
 * 获取符合回收员接单的订单
 */
export const reqRetrieveMatchingOrders = (data) => {
  return http.post(urlPrefix + '/order/matching', data)
}

/**
 * 根据订单ID获取订单详情
 */
export const reqOrderDetails = (orderId) => {
  return http.get(urlPrefix + `/order/details/${orderId}`)
}

/**
 * 回收员抢单
 */
export const reqGrabOrder = (orderId) => {
  return http.post(urlPrefix + `/order/grab/${orderId}`)
}

/**
 * 根据订单状体啊获取订单列表
 */
export const reqOrderListByStaus = (status) => {
  return http.get(urlPrefix + `/order/list/${status}`)
}