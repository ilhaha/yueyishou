<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-model-item label="服务费抵扣券类型 1 折扣 2 免单" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="couponType">
              <a-input-number v-model="model.couponType" placeholder="请输入服务费抵扣券类型 1 折扣 2 免单" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="服务费抵扣券名字" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="name">
              <a-input v-model="model.name" placeholder="请输入服务费抵扣券名字"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="金额" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="amount">
              <a-input-number v-model="model.amount" placeholder="请输入金额" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="折扣：取值[1 到 10]" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="discount">
              <a-input-number v-model="model.discount" placeholder="请输入折扣：取值[1 到 10]" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="使用门槛 0->没门槛" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="conditionAmount">
              <a-input-number v-model="model.conditionAmount" placeholder="请输入使用门槛 0->没门槛" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="每人限领张数，0-不限制 1-限领1张 2-限领2张..." :labelCol="labelCol" :wrapperCol="wrapperCol" prop="perLimit">
              <a-input-number v-model="model.perLimit" placeholder="请输入每人限领张数，0-不限制 1-限领1张 2-限领2张..." style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="已使用数量" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="useCount">
              <a-input-number v-model="model.useCount" placeholder="请输入已使用数量" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="领取数量" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="receiveCount">
              <a-input-number v-model="model.receiveCount" placeholder="请输入领取数量" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="过期时间" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="expireTime">
              <j-date placeholder="请选择过期时间" v-model="model.expireTime"  style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="服务费抵扣券描述" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="description">
              <a-input v-model="model.description" placeholder="请输入服务费抵扣券描述"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="状态[0-未发布，1-已发布， -1-已过期]" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="status">
              <a-input-number v-model="model.status" placeholder="请输入状态[0-未发布，1-已发布， -1-已过期]" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="删除标记（0:未删除 1:已删除）" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="isDeleted">
              <a-input-number v-model="model.isDeleted" placeholder="请输入删除标记（0:未删除 1:已删除）" style="width: 100%" />
            </a-form-model-item>
          </a-col>
        </a-row>
      </a-form-model>
    </j-form-container>
  </a-spin>
</template>

<script>

  import { httpAction, getAction } from '@/api/manage'
  import { validateDuplicateValue } from '@/utils/util'

  export default {
    name: 'CouponInfoForm',
    components: {
    },
    props: {
      //表单禁用
      disabled: {
        type: Boolean,
        default: false,
        required: false
      }
    },
    data () {
      return {
        model:{
         },
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
           couponType: [
              { required: true, message: '请输入服务费抵扣券类型 1 折扣 2 免单!'},
           ],
           amount: [
              { required: true, message: '请输入金额!'},
           ],
           conditionAmount: [
              { required: true, message: '请输入使用门槛 0->没门槛!'},
           ],
           perLimit: [
              { required: true, message: '请输入每人限领张数，0-不限制 1-限领1张 2-限领2张...!'},
           ],
           useCount: [
              { required: true, message: '请输入已使用数量!'},
           ],
           receiveCount: [
              { required: true, message: '请输入领取数量!'},
           ],
           isDeleted: [
              { required: true, message: '请输入删除标记（0:未删除 1:已删除）!'},
           ],
        },
        url: {
          add: "/com.ilhaha.yueyishou.coupon/couponInfo/add",
          edit: "/com.ilhaha.yueyishou.coupon/couponInfo/edit",
          queryById: "/com.ilhaha.yueyishou.coupon/couponInfo/queryById"
        }
      }
    },
    computed: {
      formDisabled(){
        return this.disabled
      },
    },
    created () {
       //备份model原始值
      this.modelDefault = JSON.parse(JSON.stringify(this.model));
    },
    methods: {
      add () {
        this.edit(this.modelDefault);
      },
      edit (record) {
        this.model = Object.assign({}, record);
        this.visible = true;
      },
      submitForm () {
        const that = this;
        // 触发表单验证
        this.$refs.form.validate(valid => {
          if (valid) {
            that.confirmLoading = true;
            let httpurl = '';
            let method = '';
            if(!this.model.id){
              httpurl+=this.url.add;
              method = 'post';
            }else{
              httpurl+=this.url.edit;
               method = 'put';
            }
            httpAction(httpurl,this.model,method).then((res)=>{
              if(res.success){
                that.$message.success(res.message);
                that.$emit('ok');
              }else{
                that.$message.warning(res.message);
              }
            }).finally(() => {
              that.confirmLoading = false;
            })
          }
         
        })
      },
    }
  }
</script>