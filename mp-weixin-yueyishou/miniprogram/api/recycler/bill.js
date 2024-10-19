import http from '../../utils/http'

// 请求地址前缀
const urlPrefix = '/recycler-api';


/**
 * 生成账单
 */
export const reqGenerateBill = (data) => {
  return http.post(urlPrefix + '/bill/generate', data)
}