import http from '../../utils/http'

// 请求地址前缀
const urlPrefix = '/customer-api';

/**
 * 获取顾客账户信息
 */
export const reqCustomerAccountInfo = (data = {}) => {
  return http.post(urlPrefix + `/account/info`, data)
}

/**
 * 顾客提现
 */
export const reqCustomerAccountOnWithdraw = (data) => {
  return http.post(urlPrefix + `/account/onWithdraw`, data)
}