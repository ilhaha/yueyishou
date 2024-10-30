import http from '../../utils/http'

// 请求地址前缀
const urlPrefix = '/recycler-api';


/**
 * 获取回收员抵扣劵列表
 */
export const reqCouponList = () => {
  return http.get(urlPrefix + `/coupon/not/used`)
}