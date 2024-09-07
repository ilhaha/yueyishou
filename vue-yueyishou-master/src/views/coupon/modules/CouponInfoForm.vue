<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-model-item label="服务费抵扣券类型" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="couponType">
              <a-radio-group v-model:value="model.couponType">
                <a-radio :value="1">折扣</a-radio>
                <a-radio :value="2">免单</a-radio>
              </a-radio-group>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="服务费抵扣券名字" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="name">
              <a-input v-model="model.name" placeholder="请输入服务费抵扣券名字"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item
              label="折扣（取值[1 到 10]）"
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              prop="discount"
            >
              <a-input-number
                v-model="model.discount"
                :disabled="discountDisabled"
                placeholder="请输入折扣：取值[1 到 10]"
                style="width: 100%"
              />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="使用门槛" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="conditionAmount">
              <a-input-number
                v-model="model.conditionAmount"
                placeholder="请输入使用门槛（0无限制）"
                style="width: 100%"
              />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="每人限领张数" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="perLimit">
              <a-input-number
                v-model="model.perLimit"
                placeholder="请输入每人限领张数（0无限制）"
                style="width: 100%"
              />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="领取后几天到期" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="expireTime">
              <a-input-number
                v-model="model.expireTime"
                placeholder="请输入领取后几天到期（0无限制）"
                style="width: 100%"
              />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item
              label="服务费抵扣券描述"
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              prop="description"
            >
              <a-input v-model="model.description" placeholder="请输入服务费抵扣券描述"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="状态" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="status">
              <a-radio-group v-model:value="model.status">
                <a-radio :value="0">未发布</a-radio>
                <a-radio :value="1">已发布</a-radio>
                <a-radio :value="2">已过期</a-radio>
              </a-radio-group>
            </a-form-model-item>
          </a-col>
        </a-row>
      </a-form-model>
    </j-form-container>
  </a-spin>
</template>

<script>
import { httpAction } from '@/api/manage'

export default {
  name: 'CouponInfoForm',
  components: {},
  props: {
    // 表单禁用
    disabled: {
      type: Boolean,
      default: false,
      required: false,
    },
  },
  data() {
    return {
      model: {
        couponType: 1,
        discount: 0,
      },
      discountDisabled: false,
      labelCol: {
        xs: { span: 24 },
        sm: { span: 5 },
      },
      wrapperCol: {
        xs: { span: 24 },
        sm: { span: 16 },
      },
      confirmLoading: false,
      validatorRules: {
        couponType: [{ required: true, message: '请选择服务费抵扣券类型' }],
        name: [{ required: true, message: '请输入服务抵扣劵名字' }],
        conditionAmount: [{ required: true, message: '请选择使用门槛' }],
        perLimit: [{ required: true, message: '请输入每人限领张数（0无限制）' }],
      },
      url: {
        add: '/coupon/add',
        edit: '/coupon/edit',
        queryById: '/coupon/queryById',
      },
    }
  },
  computed: {
    formDisabled() {
      return this.disabled
    },
  },
  created() {
    // 备份 model 原始值
    this.modelDefault = JSON.parse(JSON.stringify(this.model))
  },
  watch: {
    'model.couponType'(newValue) {
      if (newValue === 2) {
        this.discountDisabled = true
        this.model.discount = 0
      } else {
        this.discountDisabled = false
      }
    },
  },
  methods: {
    add() {
      this.edit(this.modelDefault)
    },
    edit(record) {
      this.model = Object.assign({}, record)
      this.visible = true
    },
    submitForm() {
      const that = this
      // 触发表单验证
      this.$refs.form.validate((valid) => {
        if (valid) {
          that.confirmLoading = true
          let httpurl = ''
          let method = ''
          if (!this.model.id) {
            httpurl += this.url.add
            method = 'post'
          } else {
            httpurl += this.url.edit
            method = 'put'
          }
          httpAction(httpurl, this.model, method)
            .then((res) => {
              if (res.success) {
                that.$message.success(res.message)
                that.$emit('ok')
              } else {
                that.$message.warning(res.message)
              }
            })
            .finally(() => {
              that.confirmLoading = false
            })
        }
      })
    },
  },
}
</script>
