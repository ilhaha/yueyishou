<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-model-item label="服务费抵扣券ID" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="couponId">
              <a-input-number v-model="model.couponId" placeholder="请输入服务费抵扣券ID" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="回收员ID" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="recyclerId">
              <a-input-number v-model="model.recyclerId" placeholder="请输入回收员ID" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="优化券状态（1：未使用 2：已使用）" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="status">
              <a-input-number v-model="model.status" placeholder="请输入优化券状态（1：未使用 2：已使用）" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="领取时间" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="receiveTime">
              <j-date placeholder="请选择领取时间" v-model="model.receiveTime"  style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="使用时间" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="usedTime">
              <j-date placeholder="请选择使用时间" v-model="model.usedTime"  style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="使用的订单id" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="orderId">
              <a-input-number v-model="model.orderId" placeholder="请输入使用的订单id" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="过期时间" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="expireTime">
              <j-date placeholder="请选择过期时间" v-model="model.expireTime"  style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="删除标记（0:不可用 1:可用）" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="isDeleted">
              <a-input-number v-model="model.isDeleted" placeholder="请输入删除标记（0:不可用 1:可用）" style="width: 100%" />
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
    name: 'RecyclerCouponForm',
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
           isDeleted: [
              { required: true, message: '请输入删除标记（0:不可用 1:可用）!'},
           ],
        },
        url: {
          add: "/com.ilhaha.yueyishou.coupon/recyclerCoupon/add",
          edit: "/com.ilhaha.yueyishou.coupon/recyclerCoupon/edit",
          queryById: "/com.ilhaha.yueyishou.coupon/recyclerCoupon/queryById"
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