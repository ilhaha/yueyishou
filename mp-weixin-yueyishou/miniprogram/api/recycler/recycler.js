import http from '../../utils/http'

// 请求地址前缀
const urlPrefix = '/recycler-api';

/**
 * 获取回收员基本信息
 */
export const reqRecyclerBaseInfo = () => {
  return http.get(urlPrefix + `/recycler/base/info`)
}

