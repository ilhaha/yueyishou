import http from '../../utils/http'

// 请求地址前缀
const urlPrefix = '/customer-api';

/**
 * 获取已启用的废品分类树
 */
export const reqCategoryTree = () => {
  return http.get(urlPrefix + '/category/tree')
}

/**
 * 获取所有已启用的废品父分类
 */
export const reqCategoryParent = () => {
  return http.get(urlPrefix + '/category/parent/list')
}

/**
 * 获取父品类的所有子品类
 */
export const reqSubCategories = (parentId) => {
  return http.get(urlPrefix + `/category/sub/${parentId}`)
}