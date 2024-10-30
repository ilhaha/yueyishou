import http from '../../utils/http'

// 请求地址前缀
const urlPrefix = '/customer-api';


/**
 * 获取顾客抵扣劵列表
 */
export const reqCouponList = () => {
  return http.get(urlPrefix + `/coupon/not/used`)
}