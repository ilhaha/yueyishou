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