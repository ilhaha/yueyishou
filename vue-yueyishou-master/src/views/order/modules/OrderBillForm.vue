<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-model-item label="订单ID" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="orderId">
              <a-input-number v-model="model.orderId" placeholder="请输入订单ID" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="回收员预支付客户订单金额" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="expectCustomerAmount">
              <a-input-number v-model="model.expectCustomerAmount" placeholder="请输入回收员预支付客户订单金额" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="回收员实际支付客户订单金额" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="realCustomerAmount">
              <a-input-number v-model="model.realCustomerAmount" placeholder="请输入回收员实际支付客户订单金额" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="回收员预支付平台订单金额" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="expectPlatformAmount">
              <a-input-number v-model="model.expectPlatformAmount" placeholder="请输入回收员预支付平台订单金额" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="回收员实际支付平台订单金额" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="realPlatformAmount">
              <a-input-number v-model="model.realPlatformAmount" placeholder="请输入回收员实际支付平台订单金额" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="支付订单号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="transactionId">
              <a-input v-model="model.transactionId" placeholder="请输入支付订单号"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="回收员付款时间" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="payTime">
              <j-date placeholder="请选择回收员付款时间" v-model="model.payTime"  style="width: 100%" />
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
    name: 'OrderBillForm',
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
           orderId: [
              { required: true, message: '请输入订单ID!'},
           ],
        },
        url: {
          add: "/com.ilhaha.yueyishou.order/orderBill/add",
          edit: "/com.ilhaha.yueyishou.order/orderBill/edit",
          queryById: "/com.ilhaha.yueyishou.order/orderBill/queryById"
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