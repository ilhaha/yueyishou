<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-model-item label="客户昵称" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="nickname">
              <a-input v-model="model.nickname" placeholder="请输入客户昵称"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="性别" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="gender">
              <a-input v-model="model.gender" placeholder="请输入性别"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="头像" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="avatarUrl">
              <j-image-upload isMultiple  v-model="model.avatarUrl" ></j-image-upload>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="电话" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="phone">
              <a-input v-model="model.phone" placeholder="请输入电话"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="1有效，2禁用" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="status">
              <a-input-number v-model="model.status" placeholder="请输入1有效，2禁用" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="创建时间" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="createTime">
              <j-date placeholder="请选择创建时间" v-model="model.createTime"  style="width: 100%" />
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
    name: 'CustomerInfoForm',
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
           gender: [
              { required: true, message: '请输入性别!'},
           ],
           createTime: [
              { required: true, message: '请输入创建时间!'},
           ],
        },
        url: {
          add: "/com.ilhaha.yueyishou.customer/customerInfo/add",
          edit: "/com.ilhaha.yueyishou.customer/customerInfo/edit",
          queryById: "/com.ilhaha.yueyishou.customer/customerInfo/queryById"
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