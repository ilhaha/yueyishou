import {
  toast
} from '../../utils/extendApi';
import {
  customerCalculateDrivingLineURL,
  recyclerCalculateDrivingLineURL
} from '../../api/customer/map'
import {
  customerStore
} from '../../stores/customerStore'

import {
  ComponentWithStore
} from 'mobx-miniprogram-bindings'

ComponentWithStore({
  storeBindings: {
    store: customerStore,
    fields: ['token']
  },
  data: {
    routeInfo: null,
    // 标记点数据
    markers: [],
    polyline: [],
    scale: 13,
    latitude: null, // 地图中心点纬度
    longitude: null, // 地图中心经度
    timerId: null, // 定时器ID
  },
  properties: {
    orderInfo: {
      type: Object,
      value: null
    },
    freeCancellationTime: {
      type: String,
      value: null
    },
    operator: {
      type: String,
      value: 'customer'
    }
  },

  lifetimes: {
    attached() {
      const {
        customerPointLatitude,
        customerPointLongitude,
        recyclerId,
        customerId
      } = this.data.orderInfo;

      let calculateLineForm = {
        customerId: this.data.operator == 'customer' ? null : customerId,
        recyclerId: recyclerId,
        endPointLongitude: customerPointLongitude,
        endPointLatitude: customerPointLatitude
      }

      // 初始获取路线
      this.calculateDrivingLine(calculateLineForm);

      // 开启定时器，每30秒获取一次路线
      const timerId = setInterval(() => {
        this.calculateDrivingLine(calculateLineForm);
      }, 30000); // 每30秒执行一次

      // 保存定时器ID
      this.setData({
        timerId
      });


    },

    // 清除定时器，防止内存泄漏
    detached() {
      if (this.data.timerId) {
        clearInterval(this.data.timerId);
      }
    }
  },

  methods: {
    // 取消订单
    cancelOrder() {
      this.triggerEvent('canceOrder');
    },

    // 获取回收员到回收点的路线
    async calculateDrivingLine(calculateLineForm) {
      wx.request({
        url: this.data.operator == 'customer' ? customerCalculateDrivingLineURL : recyclerCalculateDrivingLineURL,
        method: 'POST',
        data: calculateLineForm,
        header: {
          'Content-Type': 'application/json',
          'token': this.data.token
        },
        success: (res) => {
          if (res.data.data) {
            const coors = res.data.data.polyline;
            const points = [];
            // 解压路线坐标数据
            const kr = 1000000;
            for (let i = 2; i < coors.length; i++) {
              coors[i] = Number(coors[i - 2]) + Number(coors[i]) / kr;
            }

            // 生成 polyline 所需的点
            for (let i = 0; i < coors.length; i += 2) {
              points.push({
                latitude: coors[i], // 纬度
                longitude: coors[i + 1] // 经度
              });
            }

            // 设置路线、标记点和地图中心
            this.setData({
              routeInfo: res.data.data,
              latitude: (res.data.data.recyclerPointLatitude + res.data.data.endPointLatitude) / 2,
              longitude: (res.data.data.recyclerPointLongitude + res.data.data.endPointLongitude) / 2,
              polyline: [{
                points: points,
                color: '#0000FF', // 蓝色的路线颜色
                width: 6, // 设置路线的宽度
                arrowLine: true // 显示箭头，表示方向
              }],
              markers: [{
                  id: 1,
                  latitude: res.data.data.recyclerPointLatitude,
                  longitude: res.data.data.recyclerPointLongitude,
                  width: 25,
                  height: 25,
                  iconPath: "../../assets/images/common/recycle_start.png",
                  label: {
                    color: "#FF0000",
                    fontSize: 12,
                    anchorX: 0,
                    anchorY: -20
                  }
                },
                {
                  id: 2,
                  latitude: res.data.data.endPointLatitude,
                  longitude: res.data.data.endPointLongitude,
                  width: 25,
                  height: 25,
                  iconPath: "../../assets/images/common/recycle_end.png",
                  label: {
                    color: "#FF0000",
                    fontSize: 12,
                    anchorX: 0,
                    anchorY: -20
                  }
                }
              ]
            });
          } else {
            const {
              customerPointLatitude,
              customerPointLongitude
            } = this.data.orderInfo;
            this.setData({
              latitude: customerPointLatitude,
              longitude: customerPointLongitude,
              routeInfo: res.data.data,
            })
          }
        },
        fail: (error) => {
          toast({
            title: '网络异常',
            icon: 'error',
            duration: 1000
          });
          console.error('请求失败', error);
        }
      });
    },
  }
});