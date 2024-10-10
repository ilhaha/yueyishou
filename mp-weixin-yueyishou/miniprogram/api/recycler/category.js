import http from '../../utils/http'

// 请求地址前缀
const urlPrefix = '/recycler-api';

/**
 * 获取所有已启用的废品父分类
 */
export const reqCategoryParent = () => {
  return http.get(urlPrefix + '/category/parent/list')
}