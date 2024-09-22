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

export const reqCustomerLoginInfo = () => {
  return http.get(urlPrefix + `/customer/login/info`)
}
