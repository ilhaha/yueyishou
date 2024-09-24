import {
  reqAddAddress,
  reqAddressInfo,
  reqUpdateAddress
} from '../../../../api/customer/address'
import QQMapWX from '../../../../libs/qqmap-wx-jssdk'
import Schema from 'async-validator'
Page({
  // 页面的初始数据
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
    longitude: '', // 经度
    latitude: '' // 纬度
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
    } = await wx.chooseLocation()

    // 使用 reverseGeocoder 方法进行逆地址解析
    this.qqmapsdk.reverseGeocoder({
      // 传入经、纬度
      location: {
        latitude,
        longitude
      },

      // 逆地址解析成功后执行
      success: (res) => {
        // 获取选择的
        const {
          street,
          street_number
        } = res.result.address_component

        const {
          standard_address
        } = res.result.formatted_addresses

        // province 省  city 市  district 区
        const {
          province, // 省
          city, // 市
          district, // 区
          adcode, // 行政区划代码
          city_code, // 城市代码，由国家码+行政区划代码（提出城市级别）组合而来，总共为9位
          nation_code // 国家代码
        } = res.result.ad_info

        this.setData({
          // 经度
          longitude,
          // 纬度
          latitude,
          // 省级: 前两位有值，后4位置0，如，河北省: 130000
          provinceCode: adcode.replace(adcode.substring(2, 6), '0000'),
          provinceName: province,

          // 市前面多个国家代码，需要进行截取
          cityCode: city_code.slice(nation_code.length),
          cityName: city,

          // 东莞市、中山市、修州市、嘉关市 因其下无区县级，
          districtCode: district && adcode,
          districtName: district,

          // 详细地址
          address: street + street_number + name,
          fullAddress: standard_address + name
        })
      }
    })
  },

  // 保存回收地址
  async saveAddrssForm(event) {
    // 解构出省市区以及 是否是默认地址
    const {
      provinceName,
      cityName,
      districtName,
      address,
      isDefault
    } = this.data

    // 拼接完整的地址
    const fullAddress = provinceName + cityName + districtName + address

    // 合并接口请求参数
    const params = {
      ...this.data,
      fullAddress,
      isDefault: isDefault ? 1 : 0
    }

    // 调用方法对最终的请求参数进行验证
    const {
      valid
    } = await this.validateAddress(params)

    // 如果验证没有通过，不继续执行后续的逻辑
    if (!valid) return

    const res = this.addressId ? await reqUpdateAddress(params) : await reqAddAddress(params);

    if (res.code == 200) {
      wx.navigateBack({
        success: () => {
          wx.toast({
            title: this.addressId ? '修改回收地址成功' : '新增回收地址成功'
          })
        }
      })

    }
  },
  // 验证新增回收地址请求参数
  // 形参 params 是需要验证的数据
  validateAddress(params) {
    // 验证回收人，是否只包含大小写字母、数字和中文字符
    const nameRegExp = '^[a-zA-Z\\d\\u4e00-\\u9fa5]+$'

    // 验证手机号
    const phoneReg = '^1(?:3\\d|4[4-9]|5[0-35-9]|6[67]|7[0-8]|8\\d|9\\d)\\d{8}$'

    // 创建验证规则，验证规则是一个对象
    // 每一项是一个验证规则，验证规则属性需要和验证的数据进行同名
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
    }

    // 创建验证实例，并传入验证规则
    const validator = new Schema(rules)

    // 调用实例方法对数据进行验证
    // 注意：我们希望将验证结果通过 Promsie 的形式返回给函数的调用者
    return new Promise((resolve) => {
      validator.validate(params, (errors, fields) => {
        if (errors) {
          // 如果验证失败，需要给用户进行提示
          wx.toast({
            title: errors[0].message
          })

          resolve({
            valid: false
          })
        } else {
          resolve({
            valid: true
          })
        }
      })
    })
  },
  // 省市区选择
  onAddressChange(event) {
    const [provinceCode, cityCode, districtCode] = event.detail.code
    const [provinceName, cityName, districtName] = event.detail.value

    // 存储省市区对应的编码
    this.setData({
      provinceCode,
      provinceName,
      cityCode,
      cityName,
      districtName,
      districtCode
    })
  },
  // 根据id获取回收地址
  async getAddressInfoById(id) {
    if (!id) return;
    this.addressId = id;
    wx.setNavigationBarTitle({
      title: '更新回收地址',
    })

    const {
      data
    } = await reqAddressInfo(id);
    this.setData(data);
  },
  onLoad: function (options) {
    // 实例化API核心类
    this.qqmapsdk = new QQMapWX({
      key: 'RK6BZ-7VE67-QGMXL-PB44A-DJ2AQ-L5BJU'
    })
    this.getAddressInfoById(options.id)
  }
})