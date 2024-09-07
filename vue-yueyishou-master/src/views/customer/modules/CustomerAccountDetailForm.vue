<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-model-item label="顾客账户id" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="customerAccountId">
              <a-input-number v-model="model.customerAccountId" placeholder="请输入顾客账户id" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="交易内容" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="content">
              <a-input v-model="model.content" placeholder="请输入交易内容"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="交易类型： 1-回收收入 2-提现" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="tradeType">
              <a-input v-model="model.tradeType" placeholder="请输入交易类型： 1-回收收入 2-提现"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="金额" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="amount">
              <a-input-number v-model="model.amount" placeholder="请输入金额" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="交易编号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="tradeNo">
              <a-input v-model="model.tradeNo" placeholder="请输入交易编号"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="删除标志(0未删除，1已删除)" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="isDeleted">
              <a-input v-model="model.isDeleted" placeholder="请输入删除标志(0未删除，1已删除)"  ></a-input>
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
    name: 'CustomerAccountDetailForm',
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
           customerAccountId: [
              { required: true, message: '请输入顾客账户id!'},
           ],
           content: [
              { required: true, message: '请输入交易内容!'},
           ],
           tradeType: [
              { required: true, message: '请输入交易类型： 1-回收收入 2-提现!'},
           ],
           amount: [
              { required: true, message: '请输入金额!'},
           ],
           isDeleted: [
              { required: true, message: '请输入删除标志(0未删除，1已删除)!'},
           ],
        },
        url: {
          add: "/com.ilhaha.yueyishou.customer/customerAccountDetail/add",
          edit: "/com.ilhaha.yueyishou.customer/customerAccountDetail/edit",
          queryById: "/com.ilhaha.yueyishou.customer/customerAccountDetail/queryById"
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