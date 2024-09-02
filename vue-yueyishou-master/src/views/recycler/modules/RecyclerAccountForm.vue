<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-model-item label="回收员id" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="recyclerId">
              <a-input-number v-model="model.recyclerId" placeholder="请输入回收员id" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="账户总金额" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="totalAmount">
              <a-input-number v-model="model.totalAmount" placeholder="请输入账户总金额" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="上缴平台总支出" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="totalPlatformPay">
              <a-input-number v-model="model.totalPlatformPay" placeholder="请输入上缴平台总支出" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="回收总支出" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="totalRecyclePay">
              <a-input-number v-model="model.totalRecyclePay" placeholder="请输入回收总支出" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="isDeleted" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="isDeleted">
              <a-input-number v-model="model.isDeleted" placeholder="请输入isDeleted" style="width: 100%" />
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
    name: 'RecyclerAccountForm',
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
           recyclerId: [
              { required: true, message: '请输入回收员id!'},
           ],
           totalAmount: [
              { required: true, message: '请输入账户总金额!'},
           ],
           totalPlatformPay: [
              { required: true, message: '请输入上缴平台总支出!'},
           ],
           totalRecyclePay: [
              { required: true, message: '请输入回收总支出!'},
           ],
           isDeleted: [
              { required: true, message: '请输入isDeleted!'},
           ],
        },
        url: {
          add: "/com.ilhaha.yueyishou.recycler/recyclerAccount/add",
          edit: "/com.ilhaha.yueyishou.recycler/recyclerAccount/edit",
          queryById: "/com.ilhaha.yueyishou.recycler/recyclerAccount/queryById"
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