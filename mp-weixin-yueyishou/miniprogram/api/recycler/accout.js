import http from '../../utils/http'

// 请求地址前缀
const urlPrefix = '/recycler-api';

/**
 * 获取回收员账户信息
 */
export const reqRecyclerAccountInfo = (data = {}) => {
  return http.post(urlPrefix + `/account/info`, data)
}
/**
 * 回收员账号充值
 */
export const reqRecyclerAccountOnRecharge = (data) => {
  return http.post(urlPrefix + `/account/onRecharge`, data)
}

/**
 * 回收员提现
 */
export const reqRecyclerAccountOnWithdraw = (data) => {
  return http.post(urlPrefix + `/account/onWithdraw`, data)
}