import http from '../../utils/http'

// 请求地址前缀
const urlPrefix = '/customer-api';

/**
 * 获取地址列表
 */
export const reqAddressList = () => {
  return http.get(urlPrefix + '/address/list')
}

/**
 * 添加地址
 */
export const reqAddAddress = (data) => {
  return http.post(urlPrefix + '/address/add', data)
}

/**
 * 修改地址
 */
export const reqUpdateAddress = (data) => {
  return http.post(urlPrefix + '/address/edit', data)
}


/**
 * 根据id获取地址信息
 */
export const reqAddressInfo = (id) => {
  return http.get(urlPrefix + `/address/${id}`)
}

/**
 * 获取当前顾客的默认回收地址
 */
export const reqAddressDefault = () => {
  return http.get(urlPrefix + `/address/default`)
}

/** 
 * 删除地址
 */
export const reqDelAddress = (id) => {
  return http.post(urlPrefix + `/address/delete/${id}`)
}