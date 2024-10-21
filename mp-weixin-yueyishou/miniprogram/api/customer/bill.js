import http from '../../utils/http'

// 请求地址前缀
const urlPrefix = '/customer-api';

/**
 * 修改账单信息
 */
export const reqUpdateBill = (data) => {
  return http.post(urlPrefix + '/bill/update', data)
}