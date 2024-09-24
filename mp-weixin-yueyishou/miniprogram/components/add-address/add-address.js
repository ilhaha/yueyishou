import {
  reqAddAddress
} from '../../api/customer/address';
import QQMapWX from '../../libs/qqmap-wx-jssdk';
import Schema from 'async-validator';
import {
  toast
} from '../../utils/extendApi'

Component({
  data: {
    name: '', // 回收人
    phone: '', // 手机号
    provinceName: '', // 省
    provinceCode: '', // 省 编码
    cityName: '', // 市
    cityCode: '', // 市 编码
    districtName: '', // 区
    districtCode: '', // 区 编码
    address: '', // 详细地址
    fullAddress: '', // 完整地址 (省 + 市 + 区 + 详细地址)
    isDefault: false, // 设置默认地址，是否默认地址 → 0：否  1：是
    addAddressShow: false
  },

  lifetimes: {
    attached() {
      // 实例化API核心类
      this.qqmapsdk = new QQMapWX({
        key: 'RK6BZ-7VE67-QGMXL-PB44A-DJ2AQ-L5BJU'
      });
    }
  },

  methods: {
    // 清空数据的方法
    clearData() {
      this.setData({
        name: '',
        phone: '',
        provinceName: '',
        provinceCode: '',
        cityName: '',
        cityCode: '',
        districtName: '',
        districtCode: '',
        address: '',
        fullAddress: '',
        isDefault: false
      });
    },
    // 切换默认地址事件处理函数
    onDefaultChange(event) {
      this.setData({
        isDefault: event.detail.value
      });
    },

    // 获取定位信息
    async onLocation() {
      // 获取经、纬度、位置名称
      let {
        latitude,
        longitude,
        name
      } = await wx.chooseLocation();

      // 使用 reverseGeocoder 方法进行逆地址解析
      this.qqmapsdk.reverseGeocoder({
        location: {
          latitude,
          longitude
        },
        success: (res) => {
          const {
            street,
            street_number
          } = res.result.address_component;

          const {
            standard_address
          } = res.result.formatted_addresses;

          const {
            province,
            city,
            district,
            adcode,
            city_code,
            nation_code
          } = res.result.ad_info;

          this.setData({
            provinceCode: adcode.replace(adcode.substring(2, 6), '0000'),
            provinceName: province,
            cityCode: city_code.slice(nation_code.length),
            cityName: city,
            districtCode: district && adcode,
            districtName: district,
            address: street + street_number + name,
            fullAddress: standard_address + name
          });
        }
      });
    },

    // 保存回收地址
    async saveAddrssForm(event) {
      const {
        provinceName,
        cityName,
        districtName,
        address,
        isDefault
      } = this.data;

      const fullAddress = provinceName + cityName + districtName + address;

      const params = {
        ...this.data,
        fullAddress,
        isDefault: isDefault ? 1 : 0
      };

      const {
        valid
      } = await this.validateAddress(params);
      if (!valid) return;

      await reqAddAddress(params);
      wx.showToast({
        title: '新增回收地址成功'
      });
      this.triggerEvent('addAddressSuccess', this.data.addAddressShow);
      // 清空数据
      this.clearData();
    },

    // 验证新增回收地址请求参数
    validateAddress(params) {
      const nameRegExp = '^[a-zA-Z\\d\\u4e00-\\u9fa5]+$';
      const phoneReg = '^1(?:3\\d|4[4-9]|5[0-35-9]|6[67]|7[0-8]|8\\d|9\\d)\\d{8}$';

      const rules = {
        name: [{
            required: true,
            message: '请输入回收人姓名'
          },
          {
            pattern: nameRegExp,
            message: '回收人姓名不合法'
          }
        ],
        phone: [{
            required: true,
            message: '请输入回收人手机号'
          },
          {
            pattern: phoneReg,
            message: '手机号不合法'
          }
        ],
        provinceName: {
          required: true,
          message: '请选择回收人所在地区'
        },
        address: {
          required: true,
          message: '请输入详细地址'
        }
      };

      const validator = new Schema(rules);

      return new Promise((resolve) => {
        validator.validate(params, (errors) => {
          if (errors) {
            toast({
              title: errors[0].message
            });
            resolve({
              valid: false
            });
          } else {
            resolve({
              valid: true
            });
          }
        });
      });
    },

    // 省市区选择
    onAddressChange(event) {
      const [provinceCode, cityCode, districtCode] = event.detail.code;
      const [provinceName, cityName, districtName] = event.detail.value;

      this.setData({
        provinceCode,
        provinceName,
        cityCode,
        cityName,
        districtName,
        districtCode
      });
    },
  }
});