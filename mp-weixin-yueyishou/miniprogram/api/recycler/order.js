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

/**
 * 回收员接单后，在预约时间前一个小时取消订单时，要重新把订单给别的回收员接单
 */
export const reqRepostOrder = (orderId) => {
  return http.post(urlPrefix + `/order/repost/${orderId}`)
}

/**
 * 回收员到达回收点
 */
export const reqRecyclerArrive = (orderId) => {
  return http.post(urlPrefix + `/order/arrive/${orderId}`)
}

/**
 * 预估订单费用
 */
export const reqCalculateOrderFee = (params) => {
  return http.post(urlPrefix + `/order/calculateOrderFee`, params)
}

/**
 * 预估订单费用
 */
export const reqUpdateOrder = (params) => {
  return http.post(urlPrefix + `/order/update`, params)
}