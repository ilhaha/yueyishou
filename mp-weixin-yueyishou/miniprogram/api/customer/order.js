import http from '../../utils/http'

// 请求地址前缀
const urlPrefix = '/customer-api';

/**
 * 预估订单费用
 */
export const reqCalculateOrderFee = (params) => {
  return http.post(urlPrefix + `/order/calculateOrderFee`, params)
}

/**
 * 下单
 */
export const reqPlaceOrder = (params) => {
  return http.post(urlPrefix + `/order/place`, params)
}

/**
 * 根据订单状态获取订单列表
 */
export const reqOrderList = (status) => {
  return http.get(urlPrefix + `/order/list/${status}`)
}

/**
 * 根据订单ID获取订单详情
 */
export const reqOrderDetails = (orderId) => {
  return http.get(urlPrefix + `/order/details/${orderId}`)
}

/**
 * 根据订单ID取消订单
 */
export const reqCancelOrder = (orderId) => {
  return http.post(urlPrefix + `/order/cancel/${orderId}`)
}

/**
 * 顾客评论
 */
export const reqReview = (data) => {
  return http.post(urlPrefix + `/order/review`, data)
}