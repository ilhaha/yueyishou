import http from '../../utils/http'

// 请求地址前缀
const urlPrefix = '/customer-api';

// 获取回收员到回收点的路线
export const calculateDrivingLineURL = 'http://localhost/customer-api/map/calculateLine';