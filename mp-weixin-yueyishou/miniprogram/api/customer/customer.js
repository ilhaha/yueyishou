import http from '../../utils/http'

// 请求地址前缀
const urlPrefix = '/customer-api';

/**
 * 小程序授权登录
 * @param {*} code 
 */
export const reqLogin = (code) => {
  return http.get(urlPrefix + `/customer/login/${code}`)
}

/**
 * 获取顾客登录信息
 */
export const reqCustomerLoginInfo = () => {
  return http.get(urlPrefix + `/customer/login/info`)
}

/**
 * 认证成为回收员
 */
export const reqRecyclerAuth = (recyclerApplyForm) => {
  return http.post(urlPrefix + `/customer/auth/recycler`, recyclerApplyForm)
}

/**
 * 获取认证信息
 */
export const reqGetAuthImages = () => {
  return http.get(urlPrefix + `/customer/get/authImages`)
}

/**
 * 更新顾客头像和昵称
 */
export const reqUpdateBaseInfo = (params) => {
  return http.post(urlPrefix + `/customer/update/base/info`, params)
}